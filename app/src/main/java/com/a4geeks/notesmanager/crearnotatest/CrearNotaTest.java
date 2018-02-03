package com.a4geeks.notesmanager.crearnotatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.a4geeks.notesmanager.R;

public class CrearNotaTest extends AppCompatActivity {

    Button btGuardar;

    EditText etTitulo, etCategoria, etDescripcion;

    TextView tvResultado;

    String titulo, categoria, descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota_test);

        btGuardar = findViewById(R.id.btGuardar);
        etTitulo = findViewById(R.id.etTitulo);
        etCategoria = findViewById(R.id.etCategoria);
        etDescripcion = findViewById(R.id.etDescripcion);
        tvResultado = findViewById(R.id.tvResultado);

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titulo = etTitulo.getText().toString();
                categoria = etCategoria.getText().toString();
                descripcion = etDescripcion.getText().toString();

                if (titulo.equals("")||categoria.equals("")||descripcion.equals("")) {

                } else {

                    tvResultado.setText("Completado");

                }

            }
        });


    }
}
