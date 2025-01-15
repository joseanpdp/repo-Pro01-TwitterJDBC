package com.twitterjdbc.services;

public interface RegisterService {
    public void register(String username, String email, String password) throws Exception;
}
