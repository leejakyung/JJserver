package com.chatting.server;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.jupiter.api.Test;

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

			Element e_userList = root.getChild("userList");
			logger.info("userList");
			
			List<Element> children = e_userList.getChildren("user");
			Iterator<Element> iter = children.iterator();
			for(int i = 0; i < 1; i++) {
				if(!iter.hasNext()) {
					break;
				}

				Element e = iter.next();
				String id = e.getChildTextTrim("id");
				String pw = e.getChildTextTrim("pw");
				String name = e.getChildTextTrim("name");

				System.out.println(id);
				System.out.println(pw);
				System.out.println(name);
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
