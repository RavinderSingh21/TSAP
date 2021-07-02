package com.example.newtsap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewUserActivity extends BaseActivity {
    private static final String TAG ="NEW_ACCOUNT_CREATION" ;
    private EditText NAME,EMAIL,PASSWORD;
    private Button SIGN_UP;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private TextView ALREADY_REGISTERED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        mAuth = FirebaseAuth.getInstance();
        NAME =findViewById(R.id.ETname);
        EMAIL =findViewById(R.id.ETemail);
        PASSWORD=findViewById(R.id.ETpassword);
        SIGN_UP =findViewById(R.id.btnSignUp);
        ALREADY_REGISTERED =findViewById(R.id.Already_user);
        progressDialog = new ProgressDialog(this);
        SIGN_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_User();
            }
        });
        ALREADY_REGISTERED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Already_User();

            }
        });
    }

    private void Create_User() {
        final String name = NAME.getText().toString().trim();
        final String email = EMAIL.getText().toString().trim();
        final String password = PASSWORD.getText().toString().trim();
        final String photo=null;
        final String phone=null;
        if(name.isEmpty()){
            NAME.setError("Enter Name");
            NAME.requestFocus();
            return;
        }
        if(email.isEmpty()){
            EMAIL.setError("Enter EMAIL");
            EMAIL.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EMAIL.setError("Enter valid Email");
            EMAIL.requestFocus();
            return;
        }

        if(password.isEmpty()){
            PASSWORD.setError("Please Enter Password");
            PASSWORD.requestFocus();
            return;
        }
        if (password.length()<6){
            PASSWORD.setError("Minimum length of password is 6");
            PASSWORD.requestFocus();
            return;

        }
        progressDialog.setMessage("Validating  Please Wait...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            PhoneSignIn New_Account = new PhoneSignIn(
                                    name,
                                    email,
                                    photo,
                                    phone

                            );
                            FirebaseDatabase.getInstance().getReference("USERS_SIGN_IN")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(New_Account)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                Toast.makeText(NewUserActivity.this,"Welcome"+" "+ name ,Toast.LENGTH_LONG).show();
                                                OpenMap();
                                            }
                                        }
                                    });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(NewUserActivity.this, "Unable to create ", Toast.LENGTH_SHORT).show();


                        }

                        // ...
                    }
                });


    }



    public void OpenMap(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        Intent map =new Intent(getApplicationContext(),MapsActivity.class);
        map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(map);

    }
    public void Already_User(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        Intent user =new Intent(getApplicationContext(),MainActivity.class);
        user.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(user);

    }
}