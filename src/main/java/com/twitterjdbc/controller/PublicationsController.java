package com.twitterjdbc.controller;

import com.twitterjdbc.services.PublicationsService;

import java.sql.SQLException;
import java.util.Scanner;

public class PublicationsController {
    PublicationsService publicationsService;
    Scanner scanner;

    public PublicationsController(PublicationsService publicationsService, Scanner scanner) {
        this.publicationsService = publicationsService;
        this.scanner = scanner;
    }

    /*
        Crear un método tweetear que reciba una conexión y un String con el texto a twittear y que inserte una
        publicación con el userID estático, el texto pasado por pantalla y la fecha actual
     */
    public void post(int usuarioID) throws SQLException {
        System.out.print("¿Qué quieres publicar?:\n\t> ");
        String texto = scanner.nextLine();
        publicationsService.post(texto, usuarioID);
    }

    /*
        Crear un método showYourTweets que reciba una conexión y muestre por pantalla el id de la publicación,
        el nombre de usuario de userID, el texto y la fecha en la que se creo la publicación.
     */
    public void showYourTweets(int usuarioID) throws SQLException {
        publicationsService.showYourPublications(usuarioID, resultSet -> {
            try {
                System.out.println("[" + resultSet.getInt(1) + "] - " + resultSet.getString(2) +
                        "\n\t" + resultSet.getString(3) + "\n\t-Creada el " +
                        resultSet.getString(4) + "-");
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
        Crear un método deleteTweet que reciba una conexión y un id y que elimine la publicación cuyo id de
        publicación coincida con el id pasado como argumento Y QUE EL ID DEL USUARIO QUE LA PUBLICÓ COINCIDA
        CON EL ATRIBUTO ESTÁTICO userID.
     */
    public void deletePublication(int usuarioID) throws SQLException {
        System.out.println("-------------------------------------------------------");
        showYourTweets(usuarioID);
        System.out.println("-------------------------------------------------------");
        System.out.print("¿Qué publicacion quieres borrar?: ");
        int publicacionId = Integer.parseInt(scanner.nextLine());
        publicationsService.deletePublication(publicacionId, usuarioID);
    }

    /*
        Crear un método showTweets que reciba una conexión y muestre por pantalla el nombre del usuario, el texto
        del tweet y la fecha en la que se publicó el tweet de todos los tweets. Deben aparecer en orden de más
        nuevo a más viejo.
     */
    public void showTweets() throws SQLException {
        publicationsService.showAllPublications(resultSet -> {
            try {
                System.out.println("[" + resultSet.getInt(1) + "] - " + resultSet.getString(2) +
                        "\n\t" + resultSet.getString(3) + "\n\t-Creada el " +
                        resultSet.getString(4) + "-");
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
        Crear un método showFollowedTweets que reciba una conexión y muestre por pantalla el nombre del usuario,
        el texto del tweet y la fecha en la que se publicó el tweet de los tweets que hayan publicado las personas
        que sigues. Deben aparecer en orden de más nuevo a más viejo.
     */
    public void showFollowedTweets(int usuarioID) throws SQLException {
        publicationsService.showFollowedTweets(usuarioID, resultSet -> {
            try {
                System.out.println("[" + resultSet.getInt(1) + "] - " + resultSet.getString(2) +
                        "\n\t" + resultSet.getString(3) + "\n\t-Creada el " +
                        resultSet.getString(4) + "-");
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
