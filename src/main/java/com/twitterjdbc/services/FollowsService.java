package com.twitterjdbc.services;

public interface FollowsService {

    public boolean userExistsInt(int user) throws Exception;
    public boolean userFollowdExistsInt(int user, int userID) throws Exception;
    public void follow(int userToFollow, int userID) throws Exception;

    public void unfollow(int userToUnfollow, int userID) throws Exception;

}
