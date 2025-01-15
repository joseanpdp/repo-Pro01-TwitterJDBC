package com.twitterjdbc.services;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Consumer;

public class PublicationsServiceImpl implements PublicationsService{
    Connection con;

    public PublicationsServiceImpl(Connection con) {
        this.con = con;
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


    public void mostrarTusPublicaciones(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId " +
                "WHERE publications.userId = " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while(resultado.next()) {
            consumidor.accept(resultado);
        }
        resultado.close();
        sentencia.close();
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

    public void mostrarTodasLasPublicaciones(Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId ORDER BY publications.createDate DESC;";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            consumidor.accept(resultado);
        }
        resultado.close();
        sentencia.close();
    }

    public void mostrarPublicacionesDeUsuariosSeguidos(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId " +
                "JOIN follows ON follows.userToFollowId = users.id " +
                "WHERE follows.users_id = " + usuarioID + " ORDER BY publications.createDate DESC;";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while(resultado.next()) {
            consumidor.accept(resultado);
        }
        resultado.close();
        sentencia.close();
    }
}
