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

    public void register(String username, String email, String password) throws Exception {
        String query = "INSERT INTO users (username, email, password, createDate) VALUES(?, ?, ?, ?);";
        PreparedStatement statement = con.prepareStatement(query);
        String passwordEncriptada = BCrypt.hashpw(password, BCrypt.gensalt());
        LocalDateTime registerDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formatedDate = registerDate.format(dateFormat);
        statement.setString(1, username);
        statement.setString(2, email);
        statement.setString(3, passwordEncriptada);
        statement.setString(4, formatedDate);
        statement.executeUpdate();
        statement.close();
        System.out.println("Registrado correctamente");
    }

}
