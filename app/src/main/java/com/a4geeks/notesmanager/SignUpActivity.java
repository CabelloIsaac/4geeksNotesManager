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

/**
 * Esta clase permite el registro de un usuario en la aplicación haciendo uso de Firebase Authentication.
 * El usuario deberá indicar un correo electrónico, una contraseña y confirmar esa contraseña para registrarse.
 * Si ya posee una cuenta, puede pulsar el texto mostrado que corresponda al inicio de sesión.
 *
 * @author Isaac Cabello.
 * @
 */

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SQLiteDatabase db;

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btRegistrarse;
    private TextView tvIniciarSesion;
    private String email, password, confirmPassword;

    /**
     * Método que se llama al ejecutar la clase.
     * Aquí se deben inicializar los componentes de interfaz, bases de datos y objetos globales.
     * @param savedInstanceState
     */
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


    /**
     * Muestra un SnackBar en la pantalla con un mensaje de relevancia para el usuario.
     * @param view La vista en la que se mostrará la SnackBar
     * @param message El mensaje que se mostrará
     */
    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();
    }


    /**
     * Obtiene los datos introducidos por el usuario y comprueba si no están vacíos, si la contraseña
     * es lo suficientemente larga y si estas coinciden entre sí.
     * Si alguna de estas condiciones no se cumple, muestra un mensaje al usuario indicando qué salió mal.
     *
     * Si las condiciones se cumplen, llama al método para realizar el registro del usuario.
     *
     * @param view la vista necesaria donde se mostrará el SnackBar
     */
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

    /**
     * Registra el usuario en Firebase Authentication. Si algo sale mal, muestra un mensaje al usuario.
     * Si el registro se completa correctamente, va a la pantalla principal.
     * @param email correo electrónico del usuario
     * @param password contraseña del usuario
     */
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
                            Toast.makeText(SignUpActivity.this, "No se pudo registrar.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });

    }

    /**
     * Va a la pantalla de iniciar sesión
     */
    public void signIn() {

        //Go to signIn Activity
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * Va a la pantalla principal.
     */
    private void goToMain() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
