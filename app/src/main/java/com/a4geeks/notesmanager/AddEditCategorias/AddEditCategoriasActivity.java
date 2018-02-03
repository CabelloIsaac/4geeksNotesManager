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

import com.a4geeks.notesmanager.database.DBNotesManager;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.a4geeks.notesmanager.libs.Functions;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Esta clase se encarga de la creación o modificación de las categorías, según el parámetro recibido
 * por EXTRAS. Si se recibe un "0", se creará una nota y se mostrará un formulario en blanco que
 * consta del nombre que se le dará a la categoría.
 * Cuando se termine el formulario, se pulsa sobre el botón flotante para guardar.
 *
 * Si se recibe un "1", se llena el formulario con la información de la categoría a editar, tomando como
 * referencia el ID recibido.
 *
 * 0 = Crear
 * 1 = Editar
 *
 */

public class AddEditCategoriasActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private FirebaseAuth mAuth;

    EditText etNombre;
    int ADD_EDIT_ACTION = 0;

    String nombre, usuario;
    String id;

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

        DBNotesManager formulario = new DBNotesManager(this, DBNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        //Verifica si se abrió el activity para crear (0) o editar (1)
        if (getIntent().getExtras() != null) {
            ADD_EDIT_ACTION = getIntent().getIntExtra(Constantes.ADD_EDIT_ACTION, 0);
        }

        //Verifica si está editando
        if (ADD_EDIT_ACTION == 1) {
            setTitle("Editar categoría");
            id = getIntent().getStringExtra(Constantes.ID);
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
                    crearCategoria(view);
                } else {
                    //Editando nota
                    editarCategoria(view);
                }
            }
        });

    }

    /**
     * Comprueba que el nombre no esté vacío y procede a crear una nueva categoría y guardarla en la base
     * de datos. Luego cierra la activity actual, volviendo así a la principal.
     * @param view este View es usado para mostrar el Snackbar dentro del método
     */
    private void crearCategoria(View view) {
        //INSERT INTO notas
        nombre = etNombre.getText().toString();

        if (nombre.length() < 1) {
            Functions.showSnackbar(view, "El nombre no puede estar vacío.");
        } else {

            db.execSQL("INSERT INTO " + DBNotesManager.CATEGORIA_TABLE_NAME + " ("
                    + DBNotesManager.CATEGORIA_ID_USUARIO + ", "
                    + DBNotesManager.CATEGORIA_NOMBRE + ") "
                    + "VALUES ("
                    + "'" + usuario + "', "
                    + "'" + nombre + "')");

            finish();

        }

    }

    /**
     * Comprueba que el nombre no esté vacío y procede a modificar la categoría y guardarla en la base
     * de datos. Luego cierra la activity actual, volviendo así a la principal.
     * @param view este View es usado para mostrar el Snackbar dentro del método
     */
    private void editarCategoria(View view) {

        //UPDATE notas
        nombre = etNombre.getText().toString();

        if (nombre.length() < 1) {
            Functions.showSnackbar(view, "El nombre no puede estar vacío.");
        } else {

            db.execSQL("UPDATE " + DBNotesManager.CATEGORIA_TABLE_NAME + " SET "
                    + DBNotesManager.CATEGORIA_NOMBRE + "='" + nombre + "'" +
                    " WHERE id = '" + id + "'");
            finish();

        }

    }

    /**
     * Obtiene el nombre de la categoría a modificar. Luego muestra esa información
     * en el formulario para modificarse.
     */
    private void getDataFromIDExtra() {

        //Carga la información de la nota a editar
        Cursor c = db.rawQuery("SELECT nombre FROM " + DBNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + id + "'", null);
        if (c.moveToFirst()) {
            do {

                nombre = c.getString(0);
                etNombre.setText(nombre);

            } while (c.moveToNext());
        }

    }


    /**
     * Carga en la parte superior derecha un menú.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (ADD_EDIT_ACTION==1){
            getMenuInflater().inflate(R.menu.menu_detail, menu);
        }

        return true;
    }

    /**
     * Controla si se pulsa algún botón en el menú superior derecho.
     * @param item
     * @return
     */
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

                    db.execSQL("DELETE FROM " + DBNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + AddEditCategoriasActivity.this.id + "'");
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
