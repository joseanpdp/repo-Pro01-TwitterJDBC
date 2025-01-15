package com.twitterjdbc;

import java.sql.*;
import java.util.Scanner;

import com.twitterjdbc.controller.*;
import com.twitterjdbc.services.*;


public class Main {
    static final Scanner SCANNER = new Scanner(System.in);
    static final String URL = "jdbc:mysql://localhost:3306/social_network";
    static final String USUARIO = "root";
    static final String PASSWORD = "root";
    static Connection con;
    static int usuarioID = -1;

    static RegisterController registerController;
    static LoginController loginController;
    static UsersController usersController;
    static PublicationsController publicationsController;
    static FollowsController followsController;


    public static void elegirRegistrarOiniciarSesion() throws Exception {
        boolean bandera = true;

        while (bandera) {
            System.out.println("Elige una de estas opciones: ");
            System.out.println("\t[1] Registrar");
            System.out.println("\t[2] Iniciar Sesión");
            System.out.println("\t[0] Salir");
            System.out.print("Acción: ");
            int accion = Integer.parseInt(SCANNER.nextLine());
            System.out.println();
            switch (accion) {
                case 0 -> {
                    System.out.println("Programa cerrado. Hasta luego");
                    bandera = false;
                }
                case 1 -> {
                    registerController.registrar();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 2 -> {
                    usuarioID = loginController.login();
                    System.out.println();
                    Thread.sleep(1000);
                    elegirAccionesDeUsuario();
                    Thread.sleep(1000);
                    System.out.println();
                }
                default -> System.out.println("El índice introducido no es correcto, inténtalo de nuevo.");
            }
        }
    }

    public static void elegirAccionesDeUsuario() throws Exception {
        boolean bandera = true;
        while (bandera) {
            System.out.println("Elige una de estas opciones: ");
            System.out.println("\t[1] Perfil");
            System.out.println("\t[2] Publicar");
            System.out.println("\t[3] Ver tus publicaciones");
            System.out.println("\t[4] Eliminar publicación");
            System.out.println("\t[5] Ver todas las publicaciones");
            System.out.println("\t[6] Ver todos los perfiles");
            System.out.println("\t[7] Ver un perfil");
            System.out.println("\t[8] Seguir");
            System.out.println("\t[9] Mostrar usuarios que sigues");
            System.out.println("\t[10] Dejar de seguir");
            System.out.println("\t[11] Mostrar usuarios que te siguen");
            System.out.println("\t[12] Mostrar publicaciones de usuarios seguidos");
            System.out.println("\t[0] Cerrar sesión");
            System.out.print("Acción: ");
            int accion = Integer.parseInt(SCANNER.nextLine());
            System.out.println();
            switch (accion) {
                case 0 -> {
                    System.out.println("Sesión cerrada con éxito");
                    bandera = false;
                    usuarioID = -1;
                }
                case 1 -> {
                    usersController.showMyProfile(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 2 -> {
                    publicationsController.post(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 3 -> {
                    publicationsController.showYourTweets(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 4 -> {
                    publicationsController.deletePublication(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 5 -> {
                    publicationsController.showTweets();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 6 -> {
                    usersController.showAllProfiles(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 7 -> {
                    usersController.showAProfile();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 8 -> {
                    followsController.follow(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 9 -> {
                    usersController.showYourFollows(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 10 -> {
                    followsController.unfollow(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 11 -> {
                    usersController.showYourFollowers(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 12 -> {
                    publicationsController.showFollowedTweets(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                default -> System.out.println("El índice introducido no es correcto, inténtalo de nuevo.");
            }
        }
    }

    public static Connection conectar(String url, String usuario, String password) throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }

    public static void main(String[] args) {
        try {
            con = conectar(URL, USUARIO, PASSWORD);
            registerController = new RegisterController(new RegisterServiceImpl(con), SCANNER);
            loginController = new LoginController(new LoginServiceImpl(con), SCANNER);
            usersController = new UsersController(new UsersServiceImpl(con), SCANNER);
            publicationsController = new PublicationsController(new PublicationsServiceImpl(con), SCANNER);
            followsController = new FollowsController(new FollowsServiceImpl(con), usersController, SCANNER);
            elegirRegistrarOiniciarSesion();
            elegirAccionesDeUsuario();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}