package com.twitterjdbc.services;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginServiceImpl implements LoginService {
    Connection con;

    public LoginServiceImpl(Connection con) {
        this.con = con;
    }

    public int login(String username, String password) throws Exception {
        String query = "SELECT id, password FROM users WHERE username = \"" + username + "\";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        String passwordEncriptada = resultSet.getString(2);
        if (BCrypt.checkpw(password, passwordEncriptada)) {
            System.out.println("La contraseña ingresada es correcta.");
            return resultSet.getInt(1);
        }
        resultSet.close();
        statement.close();
        throw new Exception("La contraseña no es correcta.");
    }
}
