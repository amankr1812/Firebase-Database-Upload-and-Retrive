package com.example.amank.checkdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText inputArea, inputComments,inputPhone,inputEmail,inputPassword;
    private Button btnSave,here;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    //String s="";

    String retriveemail,retrivegender,retrivephone,retrivepassword,retrivename;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        inputArea = (EditText) findViewById(R.id.area);
        inputComments = (EditText) findViewById(R.id.comments);
//        inputPhone = (EditText) findViewById(R.id.phone);
//        inputEmail = (EditText) findViewById(R.id.email);
//        inputPassword = (EditText) findViewById(R.id.password);
    btnSave = (Button) findViewById(R.id.btn_save);
//        here = (Button) findViewById(R.id.here);


        // this button when clicked shows the name and email from the database

//        here.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "email is "+retriveemail, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "name is "+retrivename, Toast.LENGTH_SHORT).show();
//
//            }
//        });

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("CheckDatabase").setValue("checkdatabase");

        // app_title change listener
        mFirebaseInstance.getReference("CheckDatabase").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String area = inputArea.getText().toString();
                String comments = inputComments.getText().toString();
//                String phone = inputPhone.getText().toString();
//                String email = inputEmail.getText().toString();
//                String password = inputPassword.getText().toString();

                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(area, comments);
                }
//                else {
//                    updateUser(area, comments);
//                }
            }
        });


//        toggleButton();
    }

    // Changing button text
//    private void toggleButton() {
//        if (TextUtils.isEmpty(userId)) {
//            btnSave.setText("Save");
//        } else {
//
//            btnSave.setText("Update");
//        }
//    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String area,String comments) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(area, comments);
//        Toast.makeText(this, "user "+user, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "user id"+userId, Toast.LENGTH_SHORT).show();
        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                retrivename=user.area;
                retrivegender=user.comments;
//                retrivephone=user.phone;
//                retriveemail=user.email;
//                retrivepassword=user.password;
                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.area );

                // Display newly updated name and email

                // clear edit text
//                inputArea.setText("");
//                inputComments.setText("");
//                inputPhone.setText("");
//                inputEmail.setText("");
//                inputPassword.setText("");



//Get datasnapshot at your "users" root node
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                ref.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Get map of users in datasnapshot
                                collectComments((Map<String,Object>) dataSnapshot.getValue());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });









//                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String area,String comments) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(area)) {

            mFirebaseDatabase.child(userId).child("name").setValue(area);
        }

        if (!TextUtils.isEmpty(comments)) {

            mFirebaseDatabase.child(userId).child("gender").setValue(comments);
        }

//        if (!TextUtils.isEmpty(phone)) {
//
//            mFirebaseDatabase.child(userId).child("phone").setValue(phone);
//        }
//
//
//        if (!TextUtils.isEmpty(email))
//            mFirebaseDatabase.child(userId).child("email").setValue(email);
//        if (!TextUtils.isEmpty(password)) {
//
//            mFirebaseDatabase.child(userId).child("password").setValue(password);
//        }


    }

    private void collectComments(Map<String,Object> users) {

        ArrayList<String> CommentsAll = new ArrayList<>();
        ArrayList<String> AreaAll = new ArrayList<>();


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            CommentsAll.add((String) singleUser.get("comments"));
            AreaAll.add((String) singleUser.get("area"));

        }

        String[] ar=new String[CommentsAll.size()];
        String[] ar2=new String[AreaAll.size()];
        String newar;
        int pp=0,ep=0,hp=0,ap=0,pn=0,en=0,hn=0,an=0;
        String[] pos ={"good","awesome","nice","great","best","beautiful","better","very good","most awesome","clean","decent","admirable","proper","done","professional","accomplished","accomplish","happy","handsome","complete"};
        String[] neg ={"not","never","bad","worst","sad","unhappy","dirty","filthy","mess","evil","depressed","worse","annoy","annoying","irritating","neither","unprofessional","irregular","incomplete"};

       // System.out.println(CommentsAll.toString());
        //Toast.makeText(this, ""+CommentsAll.toString(), Toast.LENGTH_SHORT).show();
        if(CommentsAll.size()==AreaAll.size()) {
            for (int i = 0; i < CommentsAll.size(); i++) {


                ar[i] = CommentsAll.get(i).toString();
                newar = ar[i];

                String[] words = newar.split("\\s+");

                //************************************************POSITIVE PART**************************************************


                for (int l = 0; l < words.length; l++)
                {
                    for(int m=0;m<pos.length;m++)
                    {
                        if(words[l].equalsIgnoreCase(pos[m]))
                        {

//                            for(int n=0;n<pos.length;n++) {
//                                if (words[l + 1].equalsIgnoreCase(pos[n])) {
                                    if (AreaAll.get(i).equalsIgnoreCase("potheri"))
                                        pp++;

                                    else if (AreaAll.get(i).equalsIgnoreCase("estancia"))
                                        ep++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("hostel"))
                                        hp++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("abode"))
                                        ap++;
//                                }
//                            }

//                            for(int n=0;n<neg.length;n++) {
//                                if (words[l + 1].equalsIgnoreCase(neg[n])) {
                                    if (AreaAll.get(i).equalsIgnoreCase("potheri"))
                                        pn++;

                                    else if (AreaAll.get(i).equalsIgnoreCase("estancia"))
                                        en++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("hostel"))
                                        hn++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("abode"))
                                        an++;
//                                }
//                            }

                        }




                    }
                }//***********************************NEGATIVE PART************************************************

                for (int l = 0; l < words.length; l++)
                {
                    for(int m=0;m<neg.length;m++)
                    {
                        if(words[l].equalsIgnoreCase(neg[m]))
                        {
//                            for(int n=0;n<neg.length;n++) {
//                                if (words[l + 1].equalsIgnoreCase(neg[n])) {
                                    if (AreaAll.get(i).equalsIgnoreCase( "potheri"))
                                        pp++;

                                    else if (AreaAll.get(i).equalsIgnoreCase("estancia"))
                                        ep++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("hostel"))
                                        hp++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("abode"))
                                        ap++;
//                                }
//                            }

//                            for(int n=0;n<pos.length;n++) {
//                                if (words[l + 1].equalsIgnoreCase(pos[n])) {
                                    if (AreaAll.get(i).equalsIgnoreCase( "potheri"))
                                        pn++;

                                    else if (AreaAll.get(i).equalsIgnoreCase("estancia"))
                                        en++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("hostel"))
                                        hn++;
                                    else if (AreaAll.get(i).equalsIgnoreCase("abode"))
                                        an++;
//                                }
//                            }

                        }




                    }
                }




            }
        }
        else
            Toast.makeText(this, "size not same", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Comment Saved", Toast.LENGTH_SHORT).show();






    }



}