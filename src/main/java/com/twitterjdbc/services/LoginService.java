package com.twitterjdbc.services;

public interface LoginService {
    public int iniciarSesion(String usuario, String password) throws Exception;
}
