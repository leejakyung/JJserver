package com.chatting.server;

import com.chatting.server.model.ExampleServer;
import com.chatting.server.model.SocketThreadServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JjserverApplication {

	public static void main(String[] args) {
//		boolean isStop = false;
//
//		try(ServerSocket server = new ServerSocket(9100)){
//			while(!isStop){
//				Socket socket = server.accept();
//				Thread task = new SocketThreadServer(socket);
//				task.start();
//			}
//		}catch (IOException ie){
//			ie.printStackTrace();
//		}


		try {
			ExampleServer server = new ExampleServer(9100);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
