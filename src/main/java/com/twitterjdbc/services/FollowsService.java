package com.twitterjdbc.services;

import java.util.Scanner;

public interface FollowsService {
    public void seguir(int usuarioASeguir, int usuarioID) throws Exception;

    public void dejarDeSeguir(int usuarioADejarDeSeguir, int usuarioID) throws Exception;

}
