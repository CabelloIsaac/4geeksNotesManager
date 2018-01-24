package com.a4geeks.notesmanager.AddEditNotes;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.a4geeks.notesmanager.DataBase.dbNotesManager;
import com.a4geeks.notesmanager.ListsResources.CategoriasSpinnerAdapter;
import com.a4geeks.notesmanager.ListsResources.CategoriasSpinnerClass;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.a4geeks.notesmanager.libs.Functions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddEditActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private FirebaseAuth mAuth;

    EditText etTitulo, etDescripcion;
    Spinner cbCategoria;
    int ADD_EDIT_ACTION;

    String categoria, titulo, descripcion, usuario;
    int id;

    ArrayList<CategoriasSpinnerClass> listaCategorias = new ArrayList<CategoriasSpinnerClass>();
    CategoriasSpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getCurrentUser().getUid();

        cbCategoria = findViewById(R.id.cbCategoria);
        adapter = new CategoriasSpinnerAdapter(this, listaCategorias);

        cbCategoria.setAdapter(adapter);


        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        cbCategoria = findViewById(R.id.cbCategoria);

        dbNotesManager formulario = new dbNotesManager(this, dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        //Verifica si se abrió el activity para crear (0) o editar (1)
        if (getIntent().getExtras() != null) {
            ADD_EDIT_ACTION = getIntent().getIntExtra(Constantes.ADD_EDIT_ACTION, 0);
        }

        //Verifica si está editando
        if (ADD_EDIT_ACTION == 1) {
            setTitle("Editar nota");
            id = getIntent().getIntExtra(Constantes.ID, 0);
            getDataFromIDExtra(id);
        } else {
            setTitle("Nueva nota");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ADD_EDIT_ACTION == 0) {
                    //Creando nota
                    CrearNota(view);
                } else {
                    //Editando nota
                    EditarNota(view);
                }
            }
        });

        cargarCategorias();

    }


    private void cargarCategorias() {

        //Carga la información de la nota a editar
        Cursor c = db.rawQuery("SELECT id, nombre FROM " + dbNotesManager.CATEGORIA_TABLE_NAME + " WHERE id_usuario = '" + usuario + "'", null);

        listaCategorias.add(new CategoriasSpinnerClass("-1", "Ninguna"));

        if (c.moveToFirst()) {
            do {

                String ID = c.getString(0);
                String NOMBRE = c.getString(1);

                listaCategorias.add(new CategoriasSpinnerClass(ID, NOMBRE));

            } while (c.moveToNext());
        }

        cbCategoria.setSelection(1);

    }

    private void CrearNota(View view) {
        //INSERT INTO notas
        titulo = etTitulo.getText().toString();
        descripcion = etDescripcion.getText().toString();
        categoria = Integer.toString(cbCategoria.getSelectedItemPosition());

        if (titulo.length() < 1) {
            Functions.showSnackbar(view, "El título no puede estar vacío.");
        } else {

            //Getting UnixTime
            long DATE = System.currentTimeMillis();

            db.execSQL("INSERT INTO " + dbNotesManager.NOTAS_TABLE_NAME + " ("
                    + dbNotesManager.NOTAS_ID_CATEGORIA + ", "
                    + dbNotesManager.NOTAS_ID_USUARIO + ", "
                    + dbNotesManager.NOTAS_TITULO + ", "
                    + dbNotesManager.NOTAS_DESCRIPCION + ", "
                    + dbNotesManager.NOTAS_COMPLETADA + ", "
                    + dbNotesManager.NOTAS_DATE + ") "
                    + "VALUES ("
                    + "'" + categoria + "', "
                    + "'" + usuario + "', "
                    + "'" + titulo + "', "
                    + "'" + descripcion + "', "
                    + "'" + "0" + "', "
                    + "'" + DATE + "')");
            finish();
        }

    }

    private void EditarNota(View view) {

        //UPDATE notas
        titulo = etTitulo.getText().toString();
        descripcion = etDescripcion.getText().toString();
        categoria = Integer.toString(cbCategoria.getSelectedItemPosition());

        if (titulo.length() < 1) {
            Functions.showSnackbar(view, "El título no puede estar vacío.");
        } else {

            //Getting UnixTime
            long DATE = System.currentTimeMillis();

            db.execSQL("UPDATE " + dbNotesManager.NOTAS_TABLE_NAME + " SET "
                    + dbNotesManager.NOTAS_ID_CATEGORIA + "='" + categoria + "',"
                    + dbNotesManager.NOTAS_TITULO + "='" + titulo + "',"
                    + dbNotesManager.NOTAS_DESCRIPCION + "='" + descripcion + "',"
                    + dbNotesManager.NOTAS_DATE + "='" + DATE + "'" +
                    " WHERE id = '" + id + "'");
            finish();

        }

    }

    private void getDataFromIDExtra(int ID) {

        //Carga la información de la nota a editar
        Cursor c = db.rawQuery("SELECT id_categoria, titulo, descripcion FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id = '" + ID + "'", null);

        if (c.moveToFirst()) {
            do {

                categoria = c.getString(0);
                titulo = c.getString(1);
                descripcion = c.getString(2);
                etTitulo.setText(titulo);
                etDescripcion.setText(descripcion);

            } while (c.moveToNext());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}