package com.a4geeks.notesmanager.addeditnotes;

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

import com.a4geeks.notesmanager.database.dbNotesManager;
import com.a4geeks.notesmanager.listresources.ProvinciaCursorAdapter;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.a4geeks.notesmanager.libs.Functions;
import com.google.firebase.auth.FirebaseAuth;

public class AddEditActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private FirebaseAuth mAuth;

    EditText etTitulo, etDescripcion;
    Spinner cbCategoria;
    int ADD_EDIT_ACTION;

    String categoria, titulo, descripcion, usuario;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getCurrentUser().getUid();

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        cbCategoria = findViewById(R.id.cbCategoria);

        dbNotesManager formulario = new dbNotesManager(this, dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        //Verifica si se abrió el activity para crear (0) o editar (1)
        if (getIntent().getExtras() != null) {
            ADD_EDIT_ACTION = getIntent().getIntExtra(Constantes.ADD_EDIT_ACTION, 0);
        }

        cargarCategorias();

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

    }

    private void cargarCategorias(){
        String[] columns = new String[]{"_id", "nombre"};

        MatrixCursor matrixCursor = new MatrixCursor(columns);


        String NOMBRE;
        int ID;

        String selectQuery = "select id, nombre from categorias WHERE (id_usuario ='" + usuario + "') or (id_usuario='all')  ORDER BY id ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                ID = Integer.parseInt(cursor.getString(0));
                NOMBRE = cursor.getString(1);

                matrixCursor.addRow(new Object[]{ID, NOMBRE});

            } while (cursor.moveToNext());

            ProvinciaCursorAdapter adapter = new ProvinciaCursorAdapter(AddEditActivity.this, matrixCursor);
            cbCategoria.setAdapter(adapter);

        }

    }

    private void CrearNota(View view) {
        //INSERT INTO notas
        titulo = etTitulo.getText().toString();
        descripcion = etDescripcion.getText().toString();

        try {

            Cursor c = (Cursor) cbCategoria.getSelectedItem();
            categoria = c.getString(c.getColumnIndex("_id"));

        } catch (Exception e) {
            // Toast.makeText(FormOneStep2.this, "Error", Toast.LENGTH_SHORT).show();
        }

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

        try {

            Cursor c = (Cursor) cbCategoria.getSelectedItem();
            categoria = c.getString(c.getColumnIndex("_id"));

        } catch (Exception e) {
            // Toast.makeText(FormOneStep2.this, "Error", Toast.LENGTH_SHORT).show();
        }

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

                for (int i = 0; i < cbCategoria.getCount(); i++) {
                    Cursor cursor = (Cursor) cbCategoria.getItemAtPosition(i);
                    String temp = cursor.getString(cursor.getColumnIndex("_id"));
                    if (categoria.equals(temp)) {

                        cbCategoria.setSelection(i);

                        break;

                    }
                }

            } while (c.moveToNext());
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}