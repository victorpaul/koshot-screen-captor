package com.sukinsan.shot.entity;

/**
 * Created by Victor on 5/21/2017.
 */

/**
 * For getting user data from redmine
 */
public class RedmineUserEntity {
    private long id;
    private String login;
    private String firstname;
    private String lastname;
    private String mail;
    private String created_on;
    private String last_login_on;
    private String api_key;

    public RedmineUserEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getLast_login_on() {
        return last_login_on;
    }

    public void setLast_login_on(String last_login_on) {
        this.last_login_on = last_login_on;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    @Override
    public String toString() {
        return "RedmineUserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mail='" + mail + '\'' +
                ", created_on='" + created_on + '\'' +
                ", last_login_on='" + last_login_on + '\'' +
                ", api_key='" + api_key + '\'' +
                '}';
    }
}
