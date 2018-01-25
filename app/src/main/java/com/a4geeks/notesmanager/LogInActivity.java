package com.a4geeks.notesmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a4geeks.notesmanager.DataBase.dbNotesManager;
import com.a4geeks.notesmanager.Main.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static com.a4geeks.notesmanager.libs.Functions.showSnackbar;

public class LogInActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private FirebaseAuth mAuth;

    EditText etCorreo, etPassword;
    Button btIniciarSesion;
    TextView tvRegistrarse;
    String Correo, Password;

    CallbackManager mCallbackManager;
    LoginButton loginButton;

    private static final String EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        dbNotesManager formulario = new dbNotesManager(this, dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btIniciarSesion = findViewById(R.id.btIniciarSesion);
        tvRegistrarse = findViewById(R.id.tvRegistrarse);

        btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignIn(view); //Calls SignIn function

            }
        });

        tvRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignUp(); //Calls SignUpActivity function

            }
        });

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        // Callback registration
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            GoToMain();
        }
    }

    //Log In Function
    public void SignIn(View view) {

        //Getting text from EditText's
        Correo = etCorreo.getText().toString();
        Password = etPassword.getText().toString();

        //Checking if Email or Password aren't empty
        if (Correo.length() < 1 || Password.length() < 1) {

            showSnackbar(view, "Llene ambos campos");

        } else {

            //Checking if password is long enough
            if (Password.length() < 8) {

                showSnackbar(view, "La contraseña debe ser mayor a 8 caracteres");

            } else {

                mAuth.signInWithEmailAndPassword(Correo, Password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    GoToMain();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LogInActivity.this, "Error al iniciar sesión",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            GoToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogInActivity.this, "Error al entkkrar con Facebook",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void SignUp() {

        //Go to SignUpActivity Activity
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();

    }

    private void GoToMain() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
