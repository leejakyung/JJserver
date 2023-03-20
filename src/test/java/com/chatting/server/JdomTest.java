package com.chatting.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

			Element e_userList = root.getChild("user");
			logger.info(e_userList.getName());
	

			List<Element> children = e_userList.getChildren("user");
			

			
			Iterator<Element> iter = children.iterator();
			
		
			List<Map<String, String>> userList = new ArrayList<Map<String,String>>();		
			
			while(iter.hasNext()) {

				Element e = iter.next();
//				logger.info(e.getName()+": "+e.getValue());
				
				Map<String, String> map = new HashMap<String, String>();
				map.put(e.getName(), e.getValue());
				
				userList.add(map);
				
				logger.info(map);
				
			}



		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
