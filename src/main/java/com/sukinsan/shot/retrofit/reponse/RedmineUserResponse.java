package com.sukinsan.shot.retrofit.reponse;


import com.sukinsan.shot.entity.RedmineUserEntity;

/**
 * Created by Victor on 5/21/2017.
 */

public class RedmineUserResponse {
    private RedmineUserEntity user;

    public RedmineUserResponse() {
    }

    public RedmineUserEntity getUser() {
        return user;
    }

    public void setUser(RedmineUserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RedmineUserResponse{" +
                "user=" + user +
                '}';
    }
}