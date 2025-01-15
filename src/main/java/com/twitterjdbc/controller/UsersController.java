package com.twitterjdbc.controller;

import com.twitterjdbc.services.UsersService;
import com.twitterjdbc.services.UsersServiceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.function.Consumer;

public class UsersController {
    UsersService usersService;
    Scanner scanner;

    public UsersController(UsersService usersService, Scanner scanner) {
        this.usersService = usersService;
        this.scanner = scanner;
    }

    public void showMyProfile(int usuarioID) throws SQLException {
        usersService.mostrarTuPerfil(usuarioID, resultSet -> {
            try {
                System.out.println(resultSet.getString(1) + " | Creada el " + resultSet.getString(4));
                System.out.println("\t[" + resultSet.getString(2) + "]");
                System.out.println("\t" + resultSet.getString(3));
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
        Crear un método showAllProfiles que reciba una conexión y muestra por pantalla id, nombre de usuario,
        email, descripción y fecha de registro de todos los usuarios menos del usuario cuyo id sea idéntico
        al userID estático.
     */
    public void showAllProfiles(int usuarioID) throws SQLException {
        usersService.mostrarTodosLosPerfiles(usuarioID, resultSet -> {
            try {
                System.out.println("[" + resultSet.getString(1) + "] - " + resultSet.getString(2) + " | Creada el " + resultSet.getString(5));
                System.out.println("\t[" + resultSet.getString(3) + "]");
                System.out.println("\t" + resultSet.getString(4));
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
        Crear un método estático showOtherProfile que dada una conexión y un nombre de usuario te muestre todos
        los datos de ese usuario.
     */
    public void showAProfile() throws Exception {
        System.out.print("Indica el nombre del usuario: ");
        String nombreUsuario = scanner.nextLine();
        if (usersService.comprobarUsuarioExistenteString(nombreUsuario)) {
            usersService.mostrarUnPerfil(nombreUsuario, resultSet -> {
                try {
                    System.out.println("[" + resultSet.getString(1) + "] - " + resultSet.getString(2) + " | Creada el " + resultSet.getString(5));
                    System.out.println("\t[" + resultSet.getString(3) + "]");
                    System.out.println("\t" + resultSet.getString(4));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else {
            System.out.println("Este usuario no existe");
        }
    }

        /*
        Crear un método showYourFollows que reciba una conexión y muestre por pantalla todos los datos de
        los usuarios que sigues.
     */
    public void showYourFollows(int usuarioID) throws SQLException {
        usersService.mostrarLosUsuariosQueSigues(usuarioID, resultSet -> {
            try {
                System.out.println("[" + resultSet.getString(1) + "] - " + resultSet.getString(2) + " | Creada el " + resultSet.getString(5));
                System.out.println("\t[" + resultSet.getString(3) + "]");
                System.out.println("\t" + resultSet.getString(4));
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
            Crear un método showYourFollowers que reciba una conexión y muestre por pantalla todos los datos de los
            usuarios que siguen al atributo userID estático.
         */
    public void showYourFollowers(int usuarioID) throws SQLException {
        usersService.mostrarLosUsuariosQueTeSiguen(usuarioID, resultSet -> {
            try {
                System.out.println("[" + resultSet.getString(1) + "] - " + resultSet.getString(2) + " | Creada el " + resultSet.getString(5));
                System.out.println("\t[" + resultSet.getString(3) + "]");
                System.out.println("\t" + resultSet.getString(4));
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
