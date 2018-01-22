package com.a4geeks.notesmanager;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a4geeks.notesmanager.Main.MainActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText etCorreo, etPassword, etConfirmPassword;
    Button btRegistrarse;
    TextView tvIniciarSesion;
    String Correo, Password, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

                    //Go to MainActivity
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);

                }


            }
        }

    }

    public void SignIn() {

        //Go to SignIn Activity
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);

    }

}
