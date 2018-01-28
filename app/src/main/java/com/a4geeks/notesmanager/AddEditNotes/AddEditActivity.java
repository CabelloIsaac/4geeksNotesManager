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

import com.a4geeks.notesmanager.database.DBNotesManager;
import com.a4geeks.notesmanager.listresources.CategoriasSpinnerItemAdapter;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.a4geeks.notesmanager.libs.Functions;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Clase que permite Crear o Editar Notas dependiendo del parámetro recibido
 * 0 = Crear
 * 1 = Editar
 *
 * En caso de Editar, se carga en su formulario la información de la Nota a editar
 */

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

        DBNotesManager dbNotesManager = new DBNotesManager(this, DBNotesManager.DB_NAME, null, 1);
        db = dbNotesManager.getWritableDatabase();

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
                    crearNota(view);
                } else {
                    //Editando nota
                    editarNota(view);
                }
            }
        });

    }

    private void cargarCategorias(){
        String[] columns = new String[]{"_id", "nombre"};

        MatrixCursor matrixCursor = new MatrixCursor(columns);


        String nombre;
        int id;

        String selectQuery = "select id, nombre from categorias WHERE (id_usuario ='" + usuario + "') or (id_usuario='all')  ORDER BY id ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                id = Integer.parseInt(cursor.getString(0));
                nombre = cursor.getString(1);

                matrixCursor.addRow(new Object[]{id, nombre});

            } while (cursor.moveToNext());

            CategoriasSpinnerItemAdapter adapter = new CategoriasSpinnerItemAdapter(AddEditActivity.this, matrixCursor);
            cbCategoria.setAdapter(adapter);

        }

    }

    private void crearNota(View view) {
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
            long date = System.currentTimeMillis();

            db.execSQL("INSERT INTO " + DBNotesManager.NOTAS_TABLE_NAME + " ("
                    + DBNotesManager.NOTAS_ID_CATEGORIA + ", "
                    + DBNotesManager.NOTAS_ID_USUARIO + ", "
                    + DBNotesManager.NOTAS_TITULO + ", "
                    + DBNotesManager.NOTAS_DESCRIPCION + ", "
                    + DBNotesManager.NOTAS_COMPLETADA + ", "
                    + DBNotesManager.NOTAS_DATE + ") "
                    + "VALUES ("
                    + "'" + categoria + "', "
                    + "'" + usuario + "', "
                    + "'" + titulo + "', "
                    + "'" + descripcion + "', "
                    + "'" + "0" + "', "
                    + "'" + date + "')");
            finish();
        }

    }

    private void editarNota(View view) {

        //UPDATE notas
        titulo = etTitulo.getText().toString();
        descripcion = etDescripcion.getText().toString();

        try {

            Cursor cursor = (Cursor) cbCategoria.getSelectedItem();
            categoria = cursor.getString(cursor.getColumnIndex("_id"));

        } catch (Exception e) {
            // Toast.makeText(FormOneStep2.this, "Error", Toast.LENGTH_SHORT).show();
        }

        if (titulo.length() < 1) {
            Functions.showSnackbar(view, "El título no puede estar vacío.");
        } else {

            //Getting UnixTime
            long date = System.currentTimeMillis();

            db.execSQL("UPDATE " + DBNotesManager.NOTAS_TABLE_NAME + " SET "
                    + DBNotesManager.NOTAS_ID_CATEGORIA + "='" + categoria + "',"
                    + DBNotesManager.NOTAS_TITULO + "='" + titulo + "',"
                    + DBNotesManager.NOTAS_DESCRIPCION + "='" + descripcion + "',"
                    + DBNotesManager.NOTAS_DATE + "='" + date + "'" +
                    " WHERE id = '" + id + "'");
            finish();

        }

    }

    private void getDataFromIDExtra(int ID) {

        //Carga la información de la nota a editar
        Cursor cursor = db.rawQuery("SELECT id_categoria, titulo, descripcion FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id = '" + ID + "'", null);

        if (cursor.moveToFirst()) {
            do {

                categoria = cursor.getString(0);
                titulo = cursor.getString(1);
                descripcion = cursor.getString(2);
                etTitulo.setText(titulo);
                etDescripcion.setText(descripcion);

                for (int i = 0; i < cbCategoria.getCount(); i++) {
                    Cursor cursorInterno = (Cursor) cbCategoria.getItemAtPosition(i);
                    String temp = cursorInterno.getString(cursorInterno.getColumnIndex("_id"));
                    if (categoria.equals(temp)) {

                        cbCategoria.setSelection(i);

                        break;

                    }
                }

            } while (cursor.moveToNext());
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}