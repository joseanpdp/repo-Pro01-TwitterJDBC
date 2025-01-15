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

    public boolean userExistsInt(int user) throws Exception {
        String query = "SELECT id FROM users";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            if (resultSet.getInt(1) == user) {
                return true;
            }
        }
        resultSet.close();
        statement.close();
        return false;
    }


    public void follow(int userToFollow, int userID) throws Exception {
        String query = "INSERT INTO follows (users_id, userToFollowId) VALUES(?, ?);";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, userID);
        statement.setInt(2, userToFollow);
        statement.executeUpdate();
        statement.close();
        System.out.println("Registrado correctamente");
    }

    public boolean userFollowdExistsInt(int user, int userID) throws Exception {
        String query = "SELECT userToFollowId FROM follows WHERE users_id = " + userID + ";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println("-------------------------");
        while(resultSet.next()) {
            if (resultSet.getInt(1) == user) {
                return true;
            }
        }
        resultSet.close();
        statement.close();
        return false;
    }

    public void unfollow(int userToUnfollow, int userID) throws Exception {
        String query = "DELETE FROM follows WHERE users_id = ? AND userToFollowId = ?;";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, userID);
        statement.setInt(2, userToUnfollow);
        int affectedRows = statement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Se ha dejado de seguir al usuario con éxito");
        }
        else {
            System.out.println("Usuario no encontrado");
        }
        statement.executeUpdate();
        statement.close();
    }
}
