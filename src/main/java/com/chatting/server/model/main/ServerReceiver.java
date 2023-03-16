package com.chatting.server.model.main;

import java.io.*;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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

            	logger.info("메세지를 수신했습니다.");

                String msg = ois.readObject().toString();
                logger.info(msg);
                
                String[] loginResult = msg.split(" ");
                String result = loginResult[0];
             
                logger.info(result);
                
                if("100".equals(result)) {
                	String id = loginResult[1];
                    String pw = loginResult[2];
                	oos.writeObject(result+" "+id+" "+pw+" "+"로그인 성공");
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
}
