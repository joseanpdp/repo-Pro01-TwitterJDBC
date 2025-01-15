package com.twitterjdbc.controller;

import com.twitterjdbc.services.FollowsService;
import com.twitterjdbc.services.UsersService;

import java.util.Scanner;

public class FollowsController {
    FollowsService followsService;
    UsersController usersController;
    Scanner scanner;

    public FollowsController(FollowsService followsService, UsersController usersController, Scanner scanner) {
        this.followsService = followsService;
        this.usersController = usersController;
        this.scanner = scanner;
    }

    /*
        Crear un método follow que reciba una conexión y un id al que seguir e insertar en la tabla follows que
        el userID sigue al id pasado como argumento.
     */
    public void follow(int usuarioID) throws Exception {
        System.out.println("----------------------");
        usersController.showAllProfiles(usuarioID);
        System.out.println("----------------------");
        System.out.print("¿A qué usuario quieres seguir?: ");
        int usuarioASeguir = Integer.parseInt(scanner.nextLine());
        if (followsService.comprobarUsuarioExistenteInt(usuarioASeguir)) {
            if (usuarioASeguir != usuarioID) {
                followsService.seguir(usuarioASeguir, usuarioID);
            } else {
                System.out.println("No puedes seguirte a ti mismo");
            }
        } else {
            System.out.println("Este usuario no existe");
        }
    }

    /*
        Crear un método unfollow que reciba una conexión y un id al que seguir y borrar en la tabla follows el
        registro que representa que el userID sigue al id pasado como argumento.
     */
    public void unfollow(int usuarioID) throws Exception {
        System.out.println("----------------------");
        usersController.showAllProfiles(usuarioID);
        System.out.println("----------------------");
        System.out.print("¿A qué usuario quieres dejar de seguir?: ");
        int usuarioADejarDeSeguir = Integer.parseInt(scanner.nextLine());
        if (followsService.comprobarUsuarioQueSiguesExistenteInt(usuarioADejarDeSeguir, usuarioID)) {
            if (usuarioADejarDeSeguir != usuarioID) {
                followsService.dejarDeSeguir(usuarioADejarDeSeguir, usuarioID);
            }
            else {
                System.out.println("No puedes dejarte de seguir a ti mismo");
            }
        }
        else {
            System.out.println("Este usuario no lo sigues o no existe");
        }
    }
}
