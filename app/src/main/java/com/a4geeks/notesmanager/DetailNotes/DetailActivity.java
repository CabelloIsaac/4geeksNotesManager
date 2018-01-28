package com.a4geeks.notesmanager.detailnotes;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.a4geeks.notesmanager.addeditnotes.AddEditActivity;
import com.a4geeks.notesmanager.database.DBNotesManager;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;

import java.text.SimpleDateFormat;

/**
* Clase que muestra la vista de detalle de las Notas
 **/

public class DetailActivity extends AppCompatActivity {

    TextView tvTitulo, tvDescripcion, tvDate;
    int id;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBNotesManager dbNotesManager = new DBNotesManager(this, DBNotesManager.DB_NAME, null, 1);
        db = dbNotesManager.getWritableDatabase();

        //Initialazing components
        tvTitulo = findViewById(R.id.tvTitulo);
        tvDate = findViewById(R.id.tvDate);
        tvDescripcion = findViewById(R.id.tvDescripcion);

        //Getting data from extras
        id = Integer.parseInt(getIntent().getExtras().get(Constantes.ID).toString());

        setDataToViews(id);

        // Lleva al usuario al menú de Edición
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
                intent.putExtra(Constantes.ADD_EDIT_ACTION, 1);
                intent.putExtra(Constantes.ID, id);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setDataToViews(int id) {

        //Getting data about id from DATABASE
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id = '" + id + "'", null);

        String categoria = "";
        String titulo;
        String descripcion;
        String date;

        if (cursor.moveToFirst()) {

            do {

                categoria = cursor.getString(1);
                titulo = cursor.getString(3);
                descripcion = cursor.getString(4);
                date = cursor.getString(6);

                //Setting ExtrasData to Views
                tvTitulo.setText(titulo);
                tvDescripcion.setText(descripcion);

                //Converting UnixTime to Normal Date
                long dv = Long.valueOf(date);
                java.util.Date df = new java.util.Date(dv);
                date = new SimpleDateFormat("dd/MM/yyyy - hh:mm a").format(df);

                tvDate.setText(date);

            } while (cursor.moveToNext());

        }

        Cursor cursorCategoriaName = db.rawQuery("SELECT nombre FROM " + DBNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + categoria + "'", null);

        if (cursorCategoriaName.moveToFirst()) {

            do {

                String nombre = cursorCategoriaName.getString(0);
                setTitle(nombre);

            } while (cursorCategoriaName.moveToNext());

        } else {
            setTitle("Sin categoría");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            adb.setTitle("¿Deseas eliminar esta nota?");
            adb.setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    db.execSQL("DELETE FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id = '" + DetailActivity.this.id + "'");
                    Toast.makeText(DetailActivity.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
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
