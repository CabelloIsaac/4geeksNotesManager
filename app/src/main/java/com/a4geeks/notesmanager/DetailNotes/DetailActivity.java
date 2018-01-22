package com.a4geeks.notesmanager.DetailNotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.a4geeks.notesmanager.AddEditNotes.AddEditActivity;
import com.a4geeks.notesmanager.DataBase.dbNotesManager;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitulo, tvDescripcion;
    String Titulo, Descripcion, Categoria;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialazing components
        tvTitulo = findViewById(R.id.tvTitulo);
        tvDescripcion = findViewById(R.id.tvDescripcion);

        //Getting data from extras
        ID = Integer.parseInt(getIntent().getExtras().get(Constantes.ID).toString());
        Titulo = getIntent().getExtras().get(dbNotesManager.NOTAS_TITULO).toString();
        Descripcion = getIntent().getExtras().get(dbNotesManager.NOTAS_DESCRIPCION).toString();
        Categoria = getIntent().getExtras().get(dbNotesManager.NOTAS_ID_CATEGORIA).toString();

        //Setting ExtrasData to Views
        setTitle(Categoria);
        tvTitulo.setText(Titulo);
        tvDescripcion.setText(Descripcion);

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
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
