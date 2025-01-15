package com.twitterjdbc.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FollowsServiceImpl implements FollowsService {
    Connection con;

    public FollowsServiceImpl(Connection con) {
        this.con = con;
    }

    public boolean comprobarUsuarioExistenteInt(int usuario) throws Exception {
        String query = "SELECT id FROM users";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while(resultado.next()) {
            if (resultado.getInt(1) == usuario) {
                return true;
            }
        }
        resultado.close();
        sentencia.close();
        return false;
    }


    public void seguir(int usuarioASeguir, int usuarioID) throws Exception {
        String query = "INSERT INTO follows (users_id, userToFollowId) VALUES(?, ?);";
        PreparedStatement sentencia = con.prepareStatement(query);
        sentencia.setInt(1, usuarioID);
        sentencia.setInt(2, usuarioASeguir);
        sentencia.executeUpdate();
        sentencia.close();
        System.out.println("Registrado correctamente");
    }

    public boolean comprobarUsuarioQueSiguesExistenteInt(int usuario, int usuarioID) throws Exception {
        String query = "SELECT userToFollowId FROM follows WHERE users_id = " + usuarioID + ";";
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

    public void dejarDeSeguir(int usuarioADejarDeSeguir, int usuarioID) throws Exception {
        String query = "DELETE FROM follows WHERE users_id = ? AND userToFollowId = ?;";
        PreparedStatement sentencia = con.prepareStatement(query);
        sentencia.setInt(1, usuarioID);
        sentencia.setInt(2, usuarioADejarDeSeguir);
        int filasAfectadas = sentencia.executeUpdate();
        if (filasAfectadas > 0) {
            System.out.println("Se ha dejado de seguir al usuario con éxito");
        }
        else {
            System.out.println("Usuario no encontrado");
        }
        sentencia.executeUpdate();
        sentencia.close();
    }
}
