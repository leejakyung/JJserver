package com.chatting.server.core;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.chatting.server.model.Protocol;
import com.chatting.server.model.User;
import com.chatting.server.service.UserService;
import com.chatting.server.service.XmlUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class ServerReceiver extends Thread{
    private static final Logger logger = LogManager.getLogger(ServerReceiver.class);

	private final UserService userService = new XmlUserService();
    private final Socket socket;
	private List<ServerReceiver> onlineList;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String client_id;


    public String getClient_id() {
		return client_id;
	}
    
	public ServerReceiver(Socket socket){
        this.socket = socket;
        try{
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

	public ServerReceiver(Socket socket, List<ServerReceiver> onlineList){
		this.onlineList = onlineList;
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

				String[] arr = msg.split(Protocol.seperator);

				switch (arr[0]){ //첫번째 코드로 메세지 실행 내용 분류
					case Protocol.checkLogin:

						String id = arr[1];
						String pw = arr[2];

						boolean result = validateUser(id, pw);
						if(result) {
							String reply = Protocol.checkLogin + Protocol.seperator + id + Protocol.seperator + "Y";
							oos.writeObject(reply); // 로그인 성공
	
							client_id = id; // 로그인 한 아이디 저장
							
							List<String> onlineUserList = new ArrayList<String>();
							List<String> offlineUserList = new ArrayList<String>();
							
							for (int i = 0; i < onlineList.size(); i++) {
								String user = onlineList.get(i).getClient_id();
								onlineUserList.add(user);
								
							}
							
					
							List<User> userList = userService.getAllUserList();

//							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < userList.size(); i++) {
								String user = userList.get(i).getId();
								if(!onlineUserList.contains(user)) {
									offlineUserList.add(user);
								}
						
							}
							
							broadcasting(onlineUserList, offlineUserList);
					


							
						} else {
							oos.writeObject(Protocol.checkLogin + Protocol.seperator + id + Protocol.seperator + "N"); // 로그인  실패 
						}
						
						break;
						
					case Protocol.logout:	
						
						id = arr[1];
						
						String reply = Protocol.logout + Protocol.seperator + id + Protocol.seperator + "Y";
						oos.writeObject(reply); // 로그아웃 성공 
						

						List<String> onlineUserList = new ArrayList<String>();
						List<String> offlineUserList = new ArrayList<String>();
						
						for (int i = 0; i < onlineList.size(); i++) {
							String user = onlineList.get(i).getClient_id();
							onlineUserList.add(user);
							if(onlineUserList.contains(id)) {
								onlineUserList.remove(id);								
							}							
							
						}
						
						for (int i = 0; i < onlineList.size(); i++) {
							if(onlineList.get(i).getClient_id().equals(id)) {
								onlineList.remove(i);
							}
						}
						
						
						
						List<User> userList = userService.getAllUserList();

						for (int i = 0; i < userList.size(); i++) {
							String user = userList.get(i).getId();
							if(!onlineUserList.contains(user)) {
								offlineUserList.add(user);
							}
					
						}
						
						
						
						sendLogoutMessage(onlineUserList, offlineUserList);
						
						
						break;
						
					case Protocol.createRoom:
						
						String myId = arr[1]; // 내 아이디
						String targetId = arr[2]; // 내가 선택한 아이디
						String room = arr [3]; // 채팅방 이름  


						oos.writeObject(Protocol.createRoom + Protocol.seperator + myId + Protocol.seperator + targetId + Protocol.seperator + room); // 채팅방 생성
					
						createChatRoomMessage(myId, targetId, room);
											
						
						oos.writeObject(Protocol.showRoom + Protocol.seperator + myId + Protocol.seperator + targetId + Protocol.seperator + room); // 채팅방목록 보여주기
						
						
						createChatRoomListMessage(myId, targetId, room);
						
						break;
						
					case Protocol.createRoomView:

						myId = arr[1]; // 내 아이디
						targetId = arr[2]; // 내가 선택한 아이디
						room = arr [3]; // 채팅방 이름  

						oos.writeObject(Protocol.createRoomView + Protocol.seperator + myId + Protocol.seperator + targetId + Protocol.seperator + room); // 채팅목록에서 입장하기 버튼으로 채팅방 생성

						createButtonChatRoomMessage(myId, targetId, room);

						break;	
						
					case Protocol.sendMessage:
						
						myId = arr[1]; // 내 아이디
						targetId = arr[2]; // 내가 선택한 아이디
						room = arr[3]; // 채팅방 이름
						String message = arr[4]; // 메세지 
						
						
						sendTargetIdMessage(myId, targetId, room, message);
						
						
						
						
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
		
		
		for (int i = 0; i < userList.size(); i++) {
			
			Map<String, String> user = new HashMap<String, String>();
			
			if(userList.get(i).get("id").contains(id)) {
				if(userList.get(i).get("pw").contains(pw)) {
					return true;
				}
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
	
	// 모든 온라인 유저에게 온라인리스트 전송
	private void broadcasting(List<String> onlineUserList, List<String> offlineUserList) throws IOException { 
		for (ServerReceiver receiver: onlineList) { 			
			receiver.getOos().writeObject(Protocol.onUser + Protocol.seperator + onlineUserList);
			receiver.getOos().writeObject(Protocol.offUser + Protocol.seperator + offlineUserList);
		}
	}
	
	// 모든 온라인 유저에게 온라인리스트 전송
	private void sendLogoutMessage(List<String> onlineUserList, List<String> offlineUserList) throws IOException { 
		for (ServerReceiver receiver: onlineList) { 			
			receiver.getOos().writeObject(Protocol.onUser + Protocol.seperator + onlineUserList);
			receiver.getOos().writeObject(Protocol.offUser + Protocol.seperator + offlineUserList);
		}
	}
	
	// 타겟아이디에게 채팅방 생성 메세지 전송
	private void createChatRoomMessage(String myId, String targetId, String room) throws IOException{ 
		for (ServerReceiver receiver : onlineList) {
			if(receiver.getClient_id().equals(targetId)){
				receiver.getOos().writeObject(Protocol.createRoom + Protocol.seperator + receiver.getClient_id() + Protocol.seperator + myId + Protocol.seperator + room);
			}
		}			
	}
	
	// 채팅목록에서 입장하기 버튼 클릭시 - 타겟아이디에게도 채팅방 생성 메세지 전송
	private void createButtonChatRoomMessage(String myId, String targetId, String room) throws IOException{ 
		for (ServerReceiver receiver : onlineList) {
			if(receiver.getClient_id().equals(targetId)){
				receiver.getOos().writeObject(Protocol.createRoomView + Protocol.seperator + receiver.getClient_id() + Protocol.seperator + myId + Protocol.seperator + room);
			}
		}			
	}
	
	// 타겟아이디에게 채팅방목록 생성 메세지 전송
	private void createChatRoomListMessage(String myId, String targetId, String room) throws IOException { 
		for(ServerReceiver receiver : onlineList) {
			if(receiver.getClient_id().equals(targetId)) {
				receiver.getOos().writeObject(Protocol.showRoom + Protocol.seperator + receiver.getClient_id() + Protocol.seperator + myId + Protocol.seperator + room);
			}
		}
		
		
	}
	
	// 타겟아이디에게 채팅메세지 전송
	private void sendTargetIdMessage(String myId, String targetId, String room, String message) throws IOException{ 
		for (ServerReceiver receiver : onlineList) {
			if(receiver.getClient_id().equals(targetId)){
				receiver.getOos().writeObject(Protocol.sendMessage + Protocol.seperator + receiver.getClient_id() + Protocol.seperator + myId + Protocol.seperator + room + Protocol.seperator + message);
			}
		}

	}
	

	public ObjectOutputStream getOos(){
		return this.oos;
	}
}
