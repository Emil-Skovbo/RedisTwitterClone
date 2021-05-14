package dk.cphbusiness.mrv.twitterclone.dto;

public class UserOverview {
    public String username;
    public String firstname;
    public String lastname;
    public String birthday;
    public Long numFollowers;
    public Long numFollowing;

    public UserOverview(String username, String firstname, String lastname, String birthday,Long numFollowers,Long numFollowing) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
    }

}
