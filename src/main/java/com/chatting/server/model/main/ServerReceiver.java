package com.chatting.server.model.main;

import java.io.*;
import java.net.Socket;

public class ServerReceiver extends Thread{

    private Socket socket;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private BufferedWriter writer;
    private BufferedReader reader;

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

                System.out.println("메세지를 수신했습니다.");

                String msg = ois.readObject().toString();
                System.out.println(msg);

                if ("회원가입 버튼 클릭".equals(msg)) {
                    String s = "환영합니다.";
                    oos.writeObject(s);
                }

                if("테스트 진행".equals(msg)){
                    String s = "테스트 대응합니다.";
                    oos.writeObject(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
}
