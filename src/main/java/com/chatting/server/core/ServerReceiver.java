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
    private List<String> targetIdList;

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
							
							List<String> onlineUserList = new ArrayList<String>();
							List<String> offlineUserList = new ArrayList<String>();
							
							client_id = id; // 로그인 한 아이디 저장
							
							for (int i = 0; i < onlineList.size(); i++) {
								String user = onlineList.get(i).getClient_id();
								onlineUserList.add(user);
								
							}
							
							oos.writeObject("120#" + onlineUserList); // 온라인 리스트 클라이언트에 전송
							
							List<User> userList = userService.getAllUserList();

//							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < userList.size(); i++) {
								String user = userList.get(i).getId();
								if(!onlineUserList.contains(user)) {
									offlineUserList.add(user);
								}
						
							}
							
							
							oos.writeObject("121#" + offlineUserList); // 오프라인 리스트 클라이언트에 전송
							
							

							

							
						} else {
							oos.writeObject(Protocol.checkLogin + Protocol.seperator + id + Protocol.seperator + "N"); // 로그인  실패 
						}
						
						break;
					case Protocol.createRoom:
						
						String myId = arr[1];
						String targetId = arr[2];
						
						targetIdList = new ArrayList<String>();
						
						if(!targetIdList.contains(targetId)) { // 새로운 채팅방을 생성해야 하는 유저
							targetIdList.add(targetId);
							oos.writeObject(Protocol.createRoom + Protocol.seperator + targetId + Protocol.seperator + "new");
						} else { // 기존에 채팅방이 있는 유저
							oos.writeObject(Protocol.createRoom + Protocol.seperator + targetId + Protocol.seperator + "exist");
						}
						
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

	private void broadcasting() throws IOException {
		for (ServerReceiver receiver: onlineList) {
			receiver.getOos().writeObject("유저리스트");
		}
	}

	public ObjectOutputStream getOos(){
		return this.oos;
	}
}
