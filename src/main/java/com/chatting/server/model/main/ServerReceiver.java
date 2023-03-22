package com.chatting.server.model.main;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.chatting.server.model.Protocol;
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
    

    @Override
    public void run() {

        boolean isStop = false;
        if (ois == null) { //무한루프 방지
            isStop = true;
        }

        try {
            run_start://while문같은 반복문 전체를 빠져 나가도록 처리할 때
            while (!isStop) {

                String msg = ois.readObject().toString(); //클라이언트로 부터 오는 메세지 수신 담당, 항상 메세지를 받은 이후부터 모든 서버업무가 수행이 가능함
				logger.info("{} - 메세지를 수신했습니다.", msg);

				String[] loginResult = msg.split(" ");

				switch (loginResult[0]){ //첫번째 코드로 메세지 실행 내용 분류
					case Protocol.checkLogin:

						String id = loginResult[1];
						String pw = loginResult[2];

					
						getUserList();

						boolean result = validateUser(id, pw);
						if(result == true) {
							oos.writeObject("로그인 성공");
						} else {
							oos.writeObject("로그인 실패");
						}
						
						break;
					case Protocol.createRoomView:


						break;
					default:

						oos.writeObject("테스트 완료");
						break;
				}


			}
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
    
	public boolean validateUser(String id, String pw){
		List<Map<String, String>> userList = getUserList();
		
		for(Iterator<Map<String, String>> iter = userList.iterator(); iter.hasNext();) {
			Map<String, String> element = iter.next();
			
			if(element.get(id).contains(id)) {
				if(element.get(pw).contains(pw)) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;

	}

	public List<Map<String, String>> getUserList(){
		List<Map<String, String>> list = new ArrayList<>();
		
		URL resource = getClass().getClassLoader().getResource("user-info.xml");
		
		try {
			Document document = new SAXBuilder().build(resource);
			Element root = document.getRootElement();
			List<Element> userList = root.getChildren("user");
			
			
			for (Element element : userList) {
				Map<String, String> user = new HashMap<>();
				user.put("id", element.getChildText("id"));
				user.put("pw", element.getChildText("pw"));
				user.put("name", element.getChildText("name"));
				
				list.add(user);
				
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return list;
	}
}
