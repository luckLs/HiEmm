package com.example.newv.comm.utli;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;

public class MD5Utli {
    public static String get_7(String s){
        return new MD5(UUID.randomUUID().toString().getBytes()).digestHex16(s).substring(2,8);
    }
}
