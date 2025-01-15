package com.twitterjdbc.services;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Consumer;

public class UsersServiceImpl implements UsersService {
    Connection con;

    public UsersServiceImpl(Connection con) {
        this.con = con;
    }
    /*
        Crear un método showYourProfile que reciba una conexión y muestre por pantalla el nombre de
        usuario, email, descripción y fecha de registro del userID estático.
     */

    public void mostrarTuPerfil(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT username, email, description, createDate FROM users WHERE id = " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        consumidor.accept(resultado);
        resultado.close();
        sentencia.close();
    }

    public void mostrarTodosLosPerfiles(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE id != " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while (resultado.next()) {
            consumidor.accept(resultado);
        }
        resultado.close();
        sentencia.close();
    }

    public boolean comprobarUsuarioExistenteString(String usuario) throws Exception {
        String query = "SELECT username FROM users";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while(resultado.next()) {
            if (resultado.getString(1).equals(usuario)) {
                return true;
            }
        }
        resultado.close();
        sentencia.close();
        return false;
    }

    public void mostrarUnPerfil(String nombreUsuario, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE username = \"" + nombreUsuario + "\";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        consumidor.accept(resultado);
        resultado.close();
        sentencia.close();
    }

    public boolean comprobarUsuarioExistenteInt(int usuario) throws Exception {
        String query = "SELECT id FROM users";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            if (resultado.getInt(1) == usuario) {
                return true;
            }
        }
        resultado.close();
        sentencia.close();
        return false;
    }

    public void mostrarLosUsuariosQueSigues(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT users.id, users.username, users.email, users.description, users.createDate " +
                "FROM users JOIN follows ON users.id = follows.userToFollowId WHERE follows.users_id = " +
                usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while (resultado.next()) {
            consumidor.accept(resultado);
        }
        resultado.close();
        sentencia.close();
    }


    public void mostrarLosUsuariosQueTeSiguen(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT users.id, users.username, users.email, users.description, users.createDate " +
                "FROM users JOIN follows ON users.id = follows.users_id WHERE follows.userToFollowId = " +
                usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            consumidor.accept(resultado);
        }
        resultado.close();
        sentencia.close();
    }
}
