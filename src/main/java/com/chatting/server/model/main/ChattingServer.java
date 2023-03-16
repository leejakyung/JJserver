package com.chatting.server.model.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ChattingServer extends Thread {
	
	private static final Logger logger = LogManager.getLogger(ChattingServer.class);

    private List<ServerReceiver> onlineList = new ArrayList<>();
    private ServerSocket serverSocket;

    public ChattingServer(int port) {
        try{
            serverSocket = new ServerSocket(port);
            initialize();
        }catch (IOException e){
        	logger.info("서버 구동에 실패했습니다.");
        }
    }

    public void initialize() {

    }

    @Override
    public void run() {
    	logger.info("연결 대기 시작");
        boolean isStop = false;
        while (!isStop) {
            try {
                Socket socket = serverSocket.accept();
                logger.info(socket.getInetAddress().getHostName() + "해당 소켓이 연결됐습니다.");

                ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();

                onlineList.add(receiver);
                logger.info("현재 연결된 클라이언트 : "+onlineList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
