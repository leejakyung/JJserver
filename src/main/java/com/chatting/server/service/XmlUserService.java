package com.chatting.server.service;

import com.chatting.server.model.User;
import com.chatting.server.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlUserService implements UserService{

    private final static Logger logger = LogManager.getLogger(XmlUserService.class);

    @Override
    public List<User> getAllUserList() {
        List<User> userList = new ArrayList<>();

        try {
            Document doc = FileUtils.getXmlDocument("user-info.xml");

            assert doc != null;

            Element root = doc.getRootElement();
            logger.info(root);

            List<?> children = root.getChildren("user");

            for(Iterator<?> iterator = children.iterator(); iterator.hasNext();){
                Element element = (Element) iterator.next();
                User user = new User();

                user.setId(element.getChildText("id"));
                user.setPw(element.getChildText("pw"));
                user.setName(element.getChildText("name"));

                userList.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public User getUserById(String id){
        User user = null;

        try {
            Document doc = FileUtils.getXmlDocument("user-info.xml");

            assert doc != null;

            Element root = doc.getRootElement();
            logger.info(root);

            List<?> list = root.getChildren("user");

            for(Iterator<?> iterator = list.iterator(); iterator.hasNext();){
                Element element = (Element) iterator.next();

                if(id.equals(element.getChildText("id"))){
                    user = new User();
                    user.setId(element.getChildText("id"));
                    user.setPw(element.getChildText("pw"));
                    user.setName(element.getChildText("name"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public List<String> getUserIdList() {
        List<String> result = new ArrayList<>();

        try {
            Document doc = FileUtils.getXmlDocument("user-info.xml");

            assert doc != null;

            Element root = doc.getRootElement();
            logger.info(root);

            List<?> children = root.getChildren();

            for(Iterator<?> iterator = children.iterator(); iterator.hasNext();){
                Element element = (Element) iterator.next();
                result.add(element.getChildText("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean existId(String id) {
        return false;
    }

    @Override
    public int existLogin(User user) {
        User result = getUserById(user.getId());

        if(result == null){ // id 일치하는 유저 없음
            return 0;
        }else{
            if (result.getPw().equals(user.getPw())) { //id 일치하고 패스워드 일치함
                return 1;
            }else{ //id는 일치하지만 패스워드 일치 안함
                return 2;
            }
        }
    }

    @Override
    /**
     * 0 : 아이디 없음
     * 1 : 로그인 성공
     * 2 : 패스워드 틀림
     */
    public int existLogin(String id, String pw) {
        User user = getUserById(id);

        if(user == null){ // id 일치하는 유저 없음
            return 0;
        }else{
            if (pw.equals(user.getPw())) { //id 일치하고 패스워드 일치함
                return 1;
            }else{ //id는 일치하지만 패스워드 일치 안함
                return 2;
            }
        }
    }

    @Override
    public int insertUser() {
        return 0;
    }

}
