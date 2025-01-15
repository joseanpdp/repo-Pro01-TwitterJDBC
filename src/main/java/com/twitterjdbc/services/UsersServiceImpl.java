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

    public void showYourProfile(int userID, Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT username, email, description, createDate FROM users WHERE id = " + userID + ";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        consumer.accept(resultSet);
        resultSet.close();
        statement.close();
    }

    public void showAllProfiles(int userID, Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE id != " + userID + ";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            consumer.accept(resultSet);
        }
        resultSet.close();
        statement.close();
    }

    public boolean userExistsString(String user) throws Exception {
        String query = "SELECT username FROM users";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            if (resultSet.getString(1).equals(user)) {
                return true;
            }
        }
        resultSet.close();
        statement.close();
        return false;
    }

    public void showAProfile(String username, Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE username = \"" + username + "\";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        consumer.accept(resultSet);
        resultSet.close();
        statement.close();
    }

    public boolean userExistsInt(int user) throws Exception {
        String query = "SELECT id FROM users";
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

    public void showFollowedUsers(int userID, Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT users.id, users.username, users.email, users.description, users.createDate " +
                "FROM users JOIN follows ON users.id = follows.userToFollowId WHERE follows.users_id = " +
                userID + ";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println("-------------------------");
        while (resultSet.next()) {
            consumer.accept(resultSet);
        }
        resultSet.close();
        statement.close();
    }


    public void showYourFollowers(int userID, Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT users.id, users.username, users.email, users.description, users.createDate " +
                "FROM users JOIN follows ON users.id = follows.users_id WHERE follows.userToFollowId = " +
                userID + ";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println("-------------------------");
        while(resultSet.next()) {
            consumer.accept(resultSet);
        }
        resultSet.close();
        statement.close();
    }
}
