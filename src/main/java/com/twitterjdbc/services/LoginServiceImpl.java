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

    public int iniciarSesion(String usuario, String password) throws Exception {
        String query = "SELECT id, password FROM users WHERE username = \"" + usuario + "\";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        String passwordEncriptada = resultado.getString(2);
        if (BCrypt.checkpw(password, passwordEncriptada)) {
            System.out.println("La contraseña ingresada es correcta.");
            return resultado.getInt(1);
        }
        resultado.close();
        sentencia.close();
        throw new Exception("La contraseña no es correcta.");
    }
}
