package com.chatting.server.model.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class User implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(User.class);

    private final String userName;
    private final Socket socket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public User(Socket socket) throws IOException {
        this.socket = socket;
        this.userName = socket.getInetAddress().getHostName();
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.oos = new ObjectOutputStream(socket.getOutputStream());

    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void test(){
        try{
            String test = "연결을 축하합니다.";
            oos.write(test.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        boolean isStop = false;
        if (ois == null || oos == null) { //무한루프 방지
            isStop = true;
        }

        try {
            run_start://while문같은 반복문 전체를 빠져 나가도록 처리할 때
            while (!isStop) {

            	logger.info("메세지를 수신했습니다.");

                String msg = ois.readObject().toString();
                logger.info(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUserName() {
        return userName;
    }

    public Socket getSocket() {
        return socket;
    }
}
