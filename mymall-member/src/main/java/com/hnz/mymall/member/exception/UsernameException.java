package com.hnz.mymall.member.exception;

/**
 * @Description:
 *
 * @createTime: 2020-06-27 16:04
 **/
public class UsernameException extends RuntimeException {


    public UsernameException() {
        super("存在相同的用户名");
    }
}
