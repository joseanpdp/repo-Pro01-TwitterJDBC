package com.twitterjdbc.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public interface UsersService {
    public void showYourProfile(int userID, Consumer<ResultSet> consumer) throws SQLException;

    public void showAllProfiles(int userID, Consumer<ResultSet> consumer) throws SQLException;

    public boolean userExistsString(String user) throws Exception;

    public void showAProfile(String username, Consumer<ResultSet> consumer) throws SQLException;

    public boolean userExistsInt(int user) throws Exception;

    public void showFollowedUsers(int userID, Consumer<ResultSet> consumer) throws SQLException;

    public void showYourFollowers(int userID, Consumer<ResultSet> consumer) throws SQLException;
}
