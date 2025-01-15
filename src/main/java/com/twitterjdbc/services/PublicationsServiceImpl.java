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

    public void post(String text, int userID) throws SQLException {
        String query = "INSERT INTO publications (userId, text, createDate) VALUES(?, ?, ?);";
        PreparedStatement statement = con.prepareStatement(query);
        LocalDateTime registerDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formatedDate = registerDate.format(dateFormat);
        statement.setInt(1, userID);
        statement.setString(2, text);
        statement.setString(3, formatedDate);
        statement.executeUpdate();
        statement.close();
        System.out.println("Publicado correctamente");
    }


    public void showYourPublications(int userID, Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId " +
                "WHERE publications.userId = " + userID + ";";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            consumer.accept(resultSet);
        }
        resultSet.close();
        statement.close();
    }


    public void deletePublication(int publicationId, int userID) throws SQLException {
        String query = "DELETE FROM publications WHERE id = ? AND userId = ?;";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, publicationId);
        statement.setInt(2, userID);
        int affectedRows = statement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Se ha eliminado la publicación con éxito");
        }
        else {
            System.out.println("Publicación no encontrada");
        }
        statement.close();
    }

    public void showAllPublications(Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId ORDER BY publications.createDate DESC;";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println("-------------------------");
        while(resultSet.next()) {
            consumer.accept(resultSet);
        }
        resultSet.close();
        statement.close();
    }

    public void showFollowedTweets(int userID, Consumer<ResultSet> consumer) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId " +
                "JOIN follows ON follows.userToFollowId = users.id " +
                "WHERE follows.users_id = " + userID + " ORDER BY publications.createDate DESC;";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            consumer.accept(resultSet);
        }
        resultSet.close();
        statement.close();
    }
}
