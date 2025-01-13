package com.twitterjdbc.controller;

import com.twitterjdbc.services.RegisterService;

import java.util.Scanner;

public class RegisterController {
    RegisterService registerService;
    Scanner scanner;

    public RegisterController(RegisterService registerService, Scanner scanner) {
        this.registerService = registerService;
        this.scanner = scanner;
    }
    /*
            Crear un método register que reciba una conexión, un usuario, un email y una contraseña y que encripte
            la contraseña e inserte los datos de usuario, teniendo en cuenta la fecha actual, mostrando un mensaje
            por pantalla del resultado de la operación.
         */
    public void registrar() throws Exception {
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        registerService.registrar(usuario, email, password);
    }
}
