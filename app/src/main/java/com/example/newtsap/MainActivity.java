package com.example.newtsap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    private EditText EMAIL,PASSWORD;
    private Button LOGIN;
    private ImageButton PHONE_SIGN_IN,GOOGLE_SIGN_IN;
    private GoogleSignInClient signInClient;
    private  int RC_SIGN_IN =1;
    private TextView REGISTER_USER;
    private String TAG ="GOOGLE AUTHENTICATION";
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        if (firstStart)
        {
            ShowChangeLanguageDialog();
        }
        setContentView(R.layout.activity_main);
        if (LocaleManager.getLanguagePref(getApplicationContext()).length() != 0)
            context = LocaleManager.setNewLocale(this,LocaleManager.getLanguagePref(getApplicationContext()));
        else
            context = LocaleManager.setNewLocale(this,LocaleManager.ENGLISH);


        mAuth = FirebaseAuth.getInstance();

        EMAIL =findViewById(R.id.editTextEmail);
        PASSWORD=findViewById(R.id.editTextPassword);
        progressDialog = new ProgressDialog(this);


        LOGIN =findViewById(R.id.sign_in);
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMap();
            }
        });


        PHONE_SIGN_IN =findViewById(R.id.Phone_Sign_In);
        GOOGLE_SIGN_IN =findViewById(R.id.google);
        REGISTER_USER=findViewById(R.id.SignUpEmail);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(MainActivity.this,gso);
        GOOGLE_SIGN_IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
        REGISTER_USER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
    }

    private void ShowChangeLanguageDialog() {
        // array of lang change in alert box
        final String[] listItems ={"हिन्दी","English"};
        AlertDialog.Builder mBuilder =new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0){
                    //hindi

                    context =LocaleManager.setNewLocale(MainActivity.this, LocaleManager.HINDI);
                    recreate();




                }
                else if (i ==1 ){
                    //english
                    context = LocaleManager.setNewLocale(MainActivity.this, LocaleManager.ENGLISH);
                    recreate();


                }



                // dismiss dialog box
                dialog.dismiss();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor =prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();


    }


    private void openRegister() {

        Intent MainIntent = new Intent(this,NewUserActivity.class);
        MainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainIntent);

    }



    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIGoogle(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
                            updateUIGoogle(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUIGoogle(FirebaseUser fuser) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            final String name =account.getDisplayName();
            String persongivenname =account.getGivenName();
            String personfamilyname =account.getFamilyName();
            final String email =account.getEmail();
            String personId =account.getId();
            final String photo = Objects.requireNonNull(account.getPhotoUrl()).toString() + "?type=small";
            final String phone =null;




            PhoneSignIn googleSignin = new PhoneSignIn(
                    name,
                    email,
                    photo,
                    phone

            );
            FirebaseDatabase.getInstance().getReference("USERS_SIGN_IN")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .setValue(googleSignin)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){


                                Toast.makeText(MainActivity.this,"Welcome"+" "+ name ,Toast.LENGTH_LONG).show();
                                OpenMap();
                            }
                        }
                    });

        }
    }
    public void OpenMap(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        Intent map =new Intent(getApplicationContext(),MapsActivity.class);
        map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(map);

    }



    private void USER_LOGIN() {
        String email = EMAIL.getText().toString().trim();
        String password  = PASSWORD.getText().toString().trim();

        //checking if email and passwords are empty
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

        //if the email and password are not empt//displaying a progress dialog

        progressDialog.setMessage("Validating  Please Wait...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });





    }

    private void updateUI(FirebaseUser fuser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            final String name = user.getDisplayName();
            final String email = user.getEmail();
            final String photo =null;
            final String phone=null;
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            PhoneSignIn googleSignin = new PhoneSignIn(
                    name,
                    email,
                    photo, phone

            );
            FirebaseDatabase.getInstance().getReference("USERS_SIGN_IN")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .setValue(googleSignin)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Welcome"+" "+ name ,Toast.LENGTH_LONG).show();
                                OpenMap();
                            }
                        }
                    });




        }
    }


}