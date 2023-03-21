package com.chatting.server.model.main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class ServerReceiver extends Thread{

    private Socket socket;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private BufferedWriter writer;
    private BufferedReader reader;

    private static final Logger logger = LogManager.getLogger(ServerReceiver.class);
    
    public ServerReceiver(Socket socket){
        this.socket = socket;
        try{
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    

    // 여기서 인자로 리턴 값을 받아서 
    @Override
    public void run() {

        boolean isStop = false;
        if (ois == null) { //무한루프 방지
            isStop = true;
        }

        try {
            run_start://while문같은 반복문 전체를 빠져 나가도록 처리할 때
            while (!isStop) {

            	logger.info("메세지를 수신했습니다.");

                String msg = ois.readObject().toString();
                logger.info(msg);
                
                String[] loginResult = msg.split(" ");
                String result = loginResult[0];
             
                logger.info(result);
                
               
                if("100".equals(result)) {
                	// if 여기들어온 아이디 패스워드가 받아온 리스트에 있으면 성공
                	String id = loginResult[1];
                    String pw = loginResult[2];
                	oos.writeObject("로그인 성공");
                	
                	// else 없으면 로그인실패 
                } 

                String s = "";

                
                if ("회원가입 버튼 클릭".equals(msg)) {
                    s = "환영합니다.";
                    oos.writeObject(s);
                } 
                
                
                if ("로그인 버튼 클릭".equals(msg)) {
                	s = "로그인 되었습니다.";
                	oos.writeObject(s);
                }

                if("테스트 진행".equals(msg)){
                    s = "테스트 대응합니다.";
                    oos.writeObject(s);
                }
                
               

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
    
    
    public List checkLogin() {
    	String filePath = "E:\\project_jklee\\jjserver\\src\\main\\resources\\user-info.xml";
    	
    	File file = new File(filePath);
    	
    	SAXBuilder saxBuilder = new SAXBuilder();
    	
		try {
			Document doc = saxBuilder.build(file);
			Element root = doc.getRootElement();
			
			List<Element> userList = root.getChildren("users");

			
			List<Map<String, String>> result = new ArrayList<Map<String,String>>();
			
			for(Iterator<Element> iter = userList.iterator(); iter.hasNext();) {
				Element element = iter.next();
				
				Map<String, String> user = new HashMap<String, String>();
				user.put("id", element.getChildText("id"));
				user.put("pw", element.getChildText("pw"));
				user.put("name", element.getChildText("name"));
				
				result.add(user);
			}
			
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 여기서 리스트 값을 넘겨줌 
		return checkLogin();
		
    }
}
