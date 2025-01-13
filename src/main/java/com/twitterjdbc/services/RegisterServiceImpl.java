package com.twitterjdbc.services;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegisterServiceImpl implements RegisterService {
    Connection con;

    public RegisterServiceImpl(Connection con) {
        this.con = con;
    }

    public void registrar(String usuario, String email, String password) throws Exception {
        String query = "INSERT INTO users (username, email, password, createDate) VALUES(?, ?, ?, ?);";
        PreparedStatement sentencia = con.prepareStatement(query);
        String passwordEncriptada = BCrypt.hashpw(password, BCrypt.gensalt());
        LocalDateTime registerDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formatedDate = registerDate.format(dateFormat);
        sentencia.setString(1, usuario);
        sentencia.setString(2, email);
        sentencia.setString(3, passwordEncriptada);
        sentencia.setString(4, formatedDate);
        sentencia.executeUpdate();
        sentencia.close();
        System.out.println("Registrado correctamente");
    }

}
