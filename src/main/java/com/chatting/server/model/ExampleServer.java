package com.chatting.server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ExampleServer extends ServerSocket implements Runnable {
    private Thread thread = null;

    public ExampleServer(int port) throws IOException {
        super(port);
        this.start();

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
        boolean isStop = false;
        while (!isStop) {
            try {
                Socket socket = this.accpet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
