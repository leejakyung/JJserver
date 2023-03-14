package com.chatting.server.model.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChattingServer extends Thread {

    private List<ServerReceiver> onlineList = new ArrayList<>();
    private ServerSocket serverSocket;

    public ChattingServer(int port) {
        try{
            serverSocket = new ServerSocket(port);
            initialize();
        }catch (IOException e){
            System.out.println("서버 구동에 실패했습니다.");
        }
    }

    public void initialize() {

    }

    @Override
    public void run() {
        System.out.println("연결 대기 시작");
        boolean isStop = false;
        while (!isStop) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress().getHostName() + "해당 소켓이 연결됐습니다.");

                ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();

                onlineList.add(receiver);
                System.out.println("현재 연결된 클라이언트 : "+onlineList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
