package com.example.amank.checkdatabase;

/**
 * Created by amank on 19-03-2017.
 */

// import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

// @IgnoreExtraProperties
public class User {

    public String area;
    public String comments;
//    public String phone;
//    public String email;
//    public String password;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String area, String comments) {
        this.area = area;
        this.comments = comments;
//        this.phone = phone;
//        this.email = email;
//        this.password = password;
    }
}