package com.a4geeks.notesmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a4geeks.notesmanager.database.DBNotesManager;
import com.a4geeks.notesmanager.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/** Activity para el registro de nuevos usuarios mediante el método de Correo Electrónico
 * y haciendo uso de Firebase de Google **/

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    SQLiteDatabase db;

    EditText etEmail, etPassword, etConfirmPassword;
    Button btRegistrarse;
    TextView tvIniciarSesion;
    String email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Instanciar base de datos
        DBNotesManager formulario = new DBNotesManager(this, DBNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        //Inicializar autenticación
        mAuth = FirebaseAuth.getInstance();

        //Iniciarlizar componentes
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btRegistrarse = findViewById(R.id.btRegistrarse);
        tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        btRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignUp(view); //Calls signIn function

            }
        });

        tvIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn(); //Calls SignUpActivity function

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Comprueba si existe una sesión activa.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            //Si hay sesión activa, envía a MainActivity
            goToMain();
        }
    }

    //SnackBar function that shows messages
    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();
    }

    //Log In Function
    public void SignUp(View view) {

        //Getting text from EditText's
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();


        //Checking if Email or password aren't empty
        if (email.length() < 1 || password.length() < 1 || confirmPassword.length() < 1) {

            showSnackbar(view, "Llene todos los campos");

        } else {

            //Checking if password is long enough
            if (password.length() < 8) {

                showSnackbar(view, "La contraseña debe ser mayor a 8 caracteres");

            } else {

                if (!password.equals(confirmPassword)) {

                    showSnackbar(view, "Las contraseñas no coinciden");

                } else {

                    RegistrarUsuario(email, password);

                }

            }
        }

    }

    private void RegistrarUsuario(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToMain();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Pro",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });

    }

    public void signIn() {

        //Go to signIn Activity
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    private void goToMain() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
