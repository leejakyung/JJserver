package com.chatting.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.jupiter.api.Test;

import com.chatting.server.model.main.User;

public class JdomTest {


	private static final Logger logger = LogManager.getLogger(JdomTest.class);

	@Test
	public void jdomTest1()  {

		String filePath = "E:\\project_jklee\\jjserver\\src\\main\\resources\\user-info.xml";

		File file = new File(filePath);

		SAXBuilder saxBuilder = new SAXBuilder();

		try {
			Document doc = saxBuilder.build(file);
			Element root = doc.getRootElement();
			logger.info(root.getName());

			List<Element> e_userList = root.getChildren("user");
			logger.info(e_userList);
	

			
			Iterator<Element> iter = e_userList.iterator();
			
		
			List<Map<String, String>> userList = new ArrayList<Map<String,String>>();		
			
			while(iter.hasNext()) {

				Element e = iter.next();
//				logger.info(e.getName()+": "+e.getValue());
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", e.getChildText("id"));
				map.put("pw", e.getChildText("pw"));
				map.put("name", e.getChildText("name"));
				
				userList.add(map);
				
				
			}
			logger.info(userList);



		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	void jsjeong() {

		try {
			URL resource = getClass().getClassLoader().getResource("user-info.xml");
			Document document = new SAXBuilder().build(resource);

			Element root = document.getRootElement();
			List<Element> userList = root.getChildren("user");

			List<Map<String, String>> result = new ArrayList<>();

			for (Iterator<Element> iterator = userList.iterator(); iterator.hasNext(); ) {
				Element element = iterator.next();

				Map<String, String> user = new HashMap<>();
				user.put("id", element.getChildText("id"));
				user.put("pw", element.getChildText("pw"));
				user.put("name", element.getChildText("name"));

				result.add(user);

			}

			System.out.println(result.size());
			System.out.println(Arrays.toString(result.toArray()));
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}
}
