package com.chatting.server;

import com.chatting.server.model.main.ChattingServer;

public class ChattingServerLauncher {

    public static void main(String[] args) {
        ChattingServer server = new ChattingServer(9100);
        server.start();
    }
}
