package com.a4geeks.notesmanager.DetailNotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.a4geeks.notesmanager.AddEditNotes.AddEditActivity;
import com.a4geeks.notesmanager.DataBase.dbNotesManager;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitulo, tvDescripcion, tvDate;
    String Titulo, Descripcion, Categoria, Date;
    int ID;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbNotesManager formulario = new dbNotesManager(this, dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        //Initialazing components
        tvTitulo = findViewById(R.id.tvTitulo);
        tvDate = findViewById(R.id.tvDate);
        tvDescripcion = findViewById(R.id.tvDescripcion);

        //Getting data from extras
        ID = Integer.parseInt(getIntent().getExtras().get(Constantes.ID).toString());

        setDateToViews(ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
                intent.putExtra(Constantes.ADD_EDIT_ACTION, 1);
                intent.putExtra(Constantes.ID, ID);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setDateToViews(int id) {

        //Getting data about ID from DATABASE
        Cursor c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id = '" + id + "'", null);
        String CATEGORIA = "";
        String TITULO = "";
        String DESCRIPCION = "";
        String DATE = "";

        if (c.moveToFirst()) {

            do {

                CATEGORIA = c.getString(1);
                TITULO = c.getString(3);
                DESCRIPCION = c.getString(4);
                DATE = c.getString(6);

                //Setting ExtrasData to Views
                tvTitulo.setText(TITULO);
                tvDescripcion.setText(DESCRIPCION);

                //Converting UnixTime to Normal Date
                long dv = Long.valueOf(DATE);
                java.util.Date df = new java.util.Date(dv);
                DATE = new SimpleDateFormat("dd/MM/yyyy - hh:mm a").format(df);

                tvDate.setText(DATE);

            } while (c.moveToNext());

        }

        Cursor cursorCategoriaName = db.rawQuery("SELECT nombre FROM " + dbNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + CATEGORIA + "'", null);

        if (cursorCategoriaName.moveToFirst()) {

            do {

                String NOMBRE = cursorCategoriaName.getString(0);
                setTitle(NOMBRE);

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

                    db.execSQL("DELETE FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id = '" + ID + "'");
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
