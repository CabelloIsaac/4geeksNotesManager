package com.a4geeks.notesmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.a4geeks.notesmanager.Main.MainActivity;

import static com.a4geeks.notesmanager.libs.Functions.showSnackbar;

public class LogInActivity extends AppCompatActivity {

    EditText etCorreo, etPassword;
    Button btIniciarSesion;
    TextView tvRegistrarse;
    String Correo, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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

        //Go to MainActivity
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);

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

                //Go to MainActivity
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);

            }
        }

    }

    public void SignUp() {

        //Go to SignUpActivity Activity
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);

    }

}
