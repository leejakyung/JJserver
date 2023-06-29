package com.chatting.server.service;

import com.chatting.server.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    //전체 유저 리스트 가져오기
    List<User> getAllUserList();

    User getUserById(String id);

    //xml을 읽고 id list 가져오기
    List<String> getUserIdList();

    //xml을 읽고 입력받은 id이 있는지 조회
    boolean existId(String id);

    //xml을 읽고 입력받은 id와 pw가 일치하는지 조회
    int existLogin(User user);

    int existLogin(String id, String pw);

    //xml에 id, pw 추가
    int insertUser();
}
