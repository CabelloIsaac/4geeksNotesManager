package com.a4geeks.notesmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a4geeks.notesmanager.DataBase.dbNotesManager;
import com.a4geeks.notesmanager.Main.MainActivity;
import com.a4geeks.notesmanager.libs.Constantes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    SQLiteDatabase db;

    EditText etCorreo, etPassword, etConfirmPassword;
    Button btRegistrarse;
    TextView tvIniciarSesion;
    String Correo, Password, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Instanciar base de datos
        dbNotesManager formulario = new dbNotesManager(this, dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        //Inicializar autenticación
        mAuth = FirebaseAuth.getInstance();

        //Iniciarlizar componentes
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btRegistrarse = findViewById(R.id.btRegistrarse);
        tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        btRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignUp(view); //Calls SignIn function

            }
        });

        tvIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignIn(); //Calls SignUpActivity function

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
            GoToMain(currentUser.getUid());
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
        Correo = etCorreo.getText().toString();
        Password = etPassword.getText().toString();
        ConfirmPassword = etConfirmPassword.getText().toString();


        //Checking if Email or Password aren't empty
        if (Correo.length() < 1 || Password.length() < 1 || ConfirmPassword.length() < 1) {

            showSnackbar(view, "Llene todos los campos");

        } else {

            //Checking if password is long enough
            if (Password.length() < 8) {

                showSnackbar(view, "La contraseña debe ser mayor a 8 caracteres");

            } else {

                if (!Password.equals(ConfirmPassword)) {

                    showSnackbar(view, "Las contraseñas no coinciden");

                } else {

                    RegistrarUsuario(Correo, Password);

                }

            }
        }

    }

    private Boolean ComprobarDisponibilidad(String email) {

        Boolean result;

        Cursor c = db.rawQuery("SELECT * FROM usuarios WHERE email = '" + email + "'", null);

        if (c.moveToFirst()) {
            result = false;
        } else {
            result = true;
        }

        return result;

    }

    private void RegistrarUsuario(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            GoToMain(user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Pro",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });

    }

    public void SignIn() {

        //Go to SignIn Activity
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    private void GoToMain(String user_id) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
