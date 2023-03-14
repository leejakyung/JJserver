package com.chatting.server.service;

import com.chatting.server.model.UserVO;

import java.util.List;

public interface DataService {


    //xml을 읽고 id list 가져오기
    List<String> getIdList();

    //xml을 읽고 입력받은 id이 있는지 조회
    boolean existId(String id);

    //xml을 읽고 입력받은 id와 pw가 일치하는지 조회
    int existLogin(UserVO user);

    int existLogin(String id, String pw);

    //xml에 id, pw 추가
    int insertUser();
}
