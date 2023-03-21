package com.chatting.server;

import org.junit.jupiter.api.Test;

public class MethodTest {

    @Test
    void test() {

        String result = getResult("", "");

        System.out.println(result);

    }


    public String getResult(String asdfag, String ghqewfs){
        if(asdfag.equals(ghqewfs)){
            return asdfag;
        }else{
            return ghqewfs;
        }
    }



    public boolean validation(int a, int b, int c){
        String str = "gas";

        String str2 = "gasd";

        String result = getResult(str, str2);

        System.out.println(result);

        return true;

    }
}
