package models;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import static utilities.Utilities.*;

@RequestScoped
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String password;
    private Integer points;
    
    private int intForRandomAvatar; // from 1 to 16 inclusive

    public User(String username, String email, String password, int points) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.points = points;
        this.intForRandomAvatar = getRandomInt(1, 16);
    }

    public User() {
        //To change body of generated methods, choose Tools | Templates.
//        throw new UnsupportedOperationException("Not supported yet."); 
        super();
        this.intForRandomAvatar = getRandomInt(1, 16);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public int getIntForRandomAvatar() {
        return intForRandomAvatar;
    }

    public void setIntForRandomAvatar(int intForRandomAvatar) {
        this.intForRandomAvatar = intForRandomAvatar;
    }

    public User copy() {
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.points = points;
        return (user);
    }

    @Override
    public String toString() {
        return ("username:" + username);
    }

}
