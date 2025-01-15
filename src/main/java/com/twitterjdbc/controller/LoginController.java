package com.twitterjdbc.controller;

import com.twitterjdbc.services.LoginService;

import java.util.Scanner;

public class LoginController {
    LoginService loginService;
    Scanner scanner;

    public LoginController(LoginService loginService, Scanner scanner) {
        this.loginService = loginService;
        this.scanner = scanner;
    }
    /*
           Crear un método login que reciba una conexión, un usuario y una contraseña y busque a dicho usuario y
           compruebe que la contraseña hasheada (la que proviene de la base de datos) coincide con la contraseña
           introducida, mostrando un mensaje de éxito. En caso contrario lanzar una excepción.
        */
    public int login() throws Exception {
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        return loginService.login(usuario, password);
    }
}
