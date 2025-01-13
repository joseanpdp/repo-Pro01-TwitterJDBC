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
    int usuarioID;

    public UsersController(UsersService usersService, Scanner scanner, int usuarioID) {
        this.usersService = usersService;
        this.scanner = scanner;
        this.usuarioID = usuarioID;
    }

    public void showMyProfile() throws SQLException {
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

    public void showAllProfiles() throws SQLException {
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
    public void showAProfile(Scanner scanner) throws Exception {
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



    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }
}
