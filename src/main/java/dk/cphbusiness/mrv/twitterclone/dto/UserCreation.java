package dk.cphbusiness.mrv.twitterclone.dto;

import java.awt.*;

public class UserCreation {
    public String username;
    public String firstname;
    public String lastname;
    public String passwordHash;
    public String birthday;
    public List follows;
    public List following;
    public String followsCount;
    public String followerCount;

    public UserCreation(String username,
                        String firstname,
                        String lastname,
                        String passwordHash,
                        String birthday
                        ) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.passwordHash = passwordHash;
        this.birthday = birthday;
        this.follows = new List();
        this.following = new List();
        this.followsCount = "0";
        this.followerCount = "0";
    }
}
