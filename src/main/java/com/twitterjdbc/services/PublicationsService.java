package com.twitterjdbc.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public interface PublicationsService {

    public void post(String text, int userID) throws SQLException;

    public void showYourPublications(int userID, Consumer<ResultSet> consumer) throws SQLException;

    public void deletePublication(int publicationId, int userID) throws SQLException;

    public void showAllPublications(Consumer<ResultSet> consumer) throws SQLException;

    public void showFollowedTweets(int userID, Consumer<ResultSet> consumer) throws SQLException;
}
