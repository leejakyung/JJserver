package com.chatting.server.sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExampleServer extends ServerSocket implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(ExampleServer.class);
	
    private Thread thread = null;

    public ExampleServer(int port) throws IOException {
        super(port);
        this.start();
        logger.info("서버소켓 시작 ");
    }

    /**
     * 서버 개시
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * 클라이언트가 접속을 할 때 실행되는 메소드
     */
    public Socket accpet() throws IOException {
        Socket chat = new Socket();
        implAccept(chat);
        return chat;
    }

    @Override
    public void run() {
    	logger.info("연결 대기 시작");
        boolean isStop = false;
        while (!isStop) {
            try {
                Socket socket = this.accept();
                logger.info(socket.getInetAddress()+"해당 소켓이 연결됐습니다.");
                Thread task = new SocketThreadServer(socket);
				task.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
