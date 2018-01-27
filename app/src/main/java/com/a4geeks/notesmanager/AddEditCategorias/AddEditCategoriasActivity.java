package com.a4geeks.notesmanager.addeditcategorias;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.a4geeks.notesmanager.database.dbNotesManager;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.a4geeks.notesmanager.libs.Functions;
import com.google.firebase.auth.FirebaseAuth;

public class AddEditCategoriasActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private FirebaseAuth mAuth;

    EditText etNombre;
    int ADD_EDIT_ACTION;

    String nombre, usuario;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getCurrentUser().getUid();

        etNombre = findViewById(R.id.etNombre);

        dbNotesManager formulario = new dbNotesManager(this, dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        //Verifica si se abrió el activity para crear (0) o editar (1)
        if (getIntent().getExtras() != null) {
            ADD_EDIT_ACTION = getIntent().getIntExtra(Constantes.ADD_EDIT_ACTION, 0);
        }

        //Verifica si está editando
        if (ADD_EDIT_ACTION == 1) {
            setTitle("Editar categoría");
            ID = getIntent().getStringExtra(Constantes.ID);
            getDataFromIDExtra();
        } else {
            setTitle("Nueva categoría");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ADD_EDIT_ACTION == 0) {
                    //Creando nota
                    CrearCategoria(view);
                } else {
                    //Editando nota
                    EditarCategoria(view);
                }
            }
        });

    }

    private void CrearCategoria(View view) {
        //INSERT INTO notas
        nombre = etNombre.getText().toString();

        if (nombre.length() < 1) {
            Functions.showSnackbar(view, "El nombre no puede estar vacío.");
        } else {

            db.execSQL("INSERT INTO " + dbNotesManager.CATEGORIA_TABLE_NAME + " ("
                    + dbNotesManager.CATEGORIA_ID_USUARIO + ", "
                    + dbNotesManager.CATEGORIA_NOMBRE + ") "
                    + "VALUES ("
                    + "'" + usuario + "', "
                    + "'" + nombre + "')");

            finish();

        }

    }

    private void EditarCategoria(View view) {

        //UPDATE notas
        nombre = etNombre.getText().toString();

        if (nombre.length() < 1) {
            Functions.showSnackbar(view, "El nombre no puede estar vacío.");
        } else {

            db.execSQL("UPDATE " + dbNotesManager.CATEGORIA_TABLE_NAME + " SET "
                    + dbNotesManager.CATEGORIA_NOMBRE + "='" + nombre + "'" +
                    " WHERE id = '" + ID + "'");
            finish();

        }

    }

    private void getDataFromIDExtra() {

        //Carga la información de la nota a editar
        Cursor c = db.rawQuery("SELECT nombre FROM " + dbNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + ID + "'", null);
        if (c.moveToFirst()) {
            do {

                nombre = c.getString(0);
                etNombre.setText(nombre);

            } while (c.moveToNext());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (ADD_EDIT_ACTION==1){
            getMenuInflater().inflate(R.menu.menu_detail, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("¿Deseas eliminar esta categoría?");
            adb.setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    db.execSQL("DELETE FROM " + dbNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + ID + "'");
                    Toast.makeText(AddEditCategoriasActivity.this, "Categoría eliminada", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });

            adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            adb.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
