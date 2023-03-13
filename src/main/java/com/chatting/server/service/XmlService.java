package com.chatting.server.service;

import com.chatting.server.model.User;

import java.util.List;

public class XmlService implements DataService {

    //xml을 읽고 id list 가져오기
    @Override
    public List<String> getIdList() {
        return null;
    }

    //xml을 읽고 입력받은 id이 있는지 조회
    @Override
    public boolean existId(String id) {

        //id가 이미 있으면 true 없으면 false 리턴
        return false;
    }

    //xml을 읽고 입력받은 id와 pw가 일치하는지 조회
    @Override
    public int existLogin(User user) {
        return 0;
    }

    @Override
    public int existLogin(String id, String pw) {
        return 0;
    }

    //xml에 id 추가
    @Override
    public int insertUser() {
        return 0;
    }


}
