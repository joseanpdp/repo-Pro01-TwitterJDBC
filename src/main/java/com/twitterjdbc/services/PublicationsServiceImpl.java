package com.twitterjdbc.services;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PublicationsServiceImpl implements PublicationsService{
    Connection con;

    public PublicationsServiceImpl(Connection con) {
        this.con = con;
    }

    /*
        Crear un método tweetear que reciba una conexión y un String con el texto a twittear y que inserte una
        publicación con el userID estático, el texto pasado por pantalla y la fecha actual
     */
    public void ejecutarPublicar(Scanner scanner, int usuarioID) throws SQLException {
        System.out.print("¿Qué quieres publicar?:\n\t> ");
        String texto = scanner.nextLine();
        publicar(texto, usuarioID);
    }

    public void publicar(String texto, int usuarioID) throws SQLException {
        String query = "INSERT INTO publications (userId, text, createDate) VALUES(?, ?, ?);";
        PreparedStatement sentencia = con.prepareStatement(query);
        LocalDateTime registerDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formatedDate = registerDate.format(dateFormat);
        sentencia.setInt(1, usuarioID);
        sentencia.setString(2, texto);
        sentencia.setString(3, formatedDate);
        sentencia.executeUpdate();
        sentencia.close();
        System.out.println("Publicado correctamente");
    }

    /*
        Crear un método showYourTweets que reciba una conexión y muestre por pantalla el id de la publicación,
        el nombre de usuario de userID, el texto y la fecha en la que se creo la publicación.
     */
    public void mostrarTusPublicaciones(int usuarioID) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId " +
                "WHERE publications.userId = " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while(resultado.next()) {
            System.out.println("[" + resultado.getInt(1) + "] - " + resultado.getString(2) +
                    "\n\t" + resultado.getString(3) + "\n\t-Creada el " +
                    resultado.getString(4) + "-");
        }
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método deleteTweet que reciba una conexión y un id y que elimine la publicación cuyo id de
        publicación coincida con el id pasado como argumento Y QUE EL ID DEL USUARIO QUE LA PUBLICÓ COINCIDA
        CON EL ATRIBUTO ESTÁTICO userID.
     */
    public void ejecutarEliminarPublicacion(Scanner scanner, int usuarioID) throws SQLException {
        System.out.println("-------------------------------------------------------");
        mostrarTusPublicaciones(usuarioID);
        System.out.println("-------------------------------------------------------");
        System.out.print("¿Qué publicacion quieres borrar?: ");
        int publicacionId = Integer.parseInt(scanner.nextLine());
        eliminarPublicacion(publicacionId, usuarioID);
    }

    public void eliminarPublicacion(int publicacionId, int usuarioID) throws SQLException {
        String query = "DELETE FROM publications WHERE id = ? AND userId = ?;";
        PreparedStatement sentencia = con.prepareStatement(query);
        sentencia.setInt(1, publicacionId);
        sentencia.setInt(2, usuarioID);
        int filasAfectadas = sentencia.executeUpdate();
        if (filasAfectadas > 0) {
            System.out.println("Se ha eliminado la publicación con éxito");
        }
        else {
            System.out.println("Publicación no encontrada");
        }
        sentencia.close();
    }

    /*
        Crear un método showTweets que reciba una conexión y muestre por pantalla el nombre del usuario, el texto
        del tweet y la fecha en la que se publicó el tweet de todos los tweets. Deben aparecer en orden de más
        nuevo a más viejo.
     */
    public void mostrarTodasLasPublicaciones() throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId ORDER BY publications.createDate DESC;";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            System.out.println("[" + resultado.getInt(1) + "] - " + resultado.getString(2) +
                    "\n\t" + resultado.getString(3) + "\n\t-Creada el " +
                    resultado.getString(4) + "-");
            System.out.println("-------------------------");
        }
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método showFollowedTweets que reciba una conexión y muestre por pantalla el nombre del usuario,
        el texto del tweet y la fecha en la que se publicó el tweet de los tweets que hayan publicado las personas
        que sigues. Deben aparecer en orden de más nuevo a más viejo.
     */
    public void mostrarPublicacionesDeUsuariosSeguidos(int usuarioID) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId " +
                "JOIN follows ON follows.userToFollowId = users.id " +
                "WHERE follows.users_id = " + usuarioID + " ORDER BY publications.createDate DESC;";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while(resultado.next()) {
            System.out.println("[" + resultado.getInt(1) + "] - " + resultado.getString(2) +
                    "\n\t" + resultado.getString(3) + "\n\t-Creada el " +
                    resultado.getString(4) + "-");
        }
        resultado.close();
        sentencia.close();
    }
}
