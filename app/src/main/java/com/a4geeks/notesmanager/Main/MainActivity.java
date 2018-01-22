package com.a4geeks.notesmanager.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a4geeks.notesmanager.R;

import java.util.ArrayList;

import static com.a4geeks.notesmanager.libs.Functions.showSnackbar;

public class MainActivity extends AppCompatActivity {

    ArrayList<ItemClass> lista = new ArrayList<ItemClass>();
    ItemAdapter adapter;
    ListView lvLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvLista = findViewById(R.id.lvLista);
        lvLista.setLongClickable(true);
        LlenarLista();

        adapter = new ItemAdapter(this, lista);

        lvLista.setAdapter(adapter);


        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ID = ((TextView) view.findViewById(R.id.tvId)).getText().toString();
                Toast.makeText(MainActivity.this, "Clic corto: " + ID, Toast.LENGTH_SHORT).show();

            }
        });

        lvLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int position, long l) {

                final String ID = ((TextView) view.findViewById(R.id.tvId)).getText().toString();

                AlertDialog alertDialog = new AlertDialog.Builder(
                        MainActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Eliminar nota");
                // Setting Dialog Message
                alertDialog.setMessage("¿Desea eliminar esta nota?");
                // Setting OK Button
                alertDialog.setButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        DeleteItem(view, ID);
                    }
                });

                // Setting Cancel Button
                alertDialog.setButton2("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                return true;
            }


        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void LlenarLista() {
        lista.add(new ItemClass("0", "Primera Nota", "Mi Primera nota", "Categoría 1"));
        lista.add(new ItemClass("1", "Segunda Nota", "Mi Segunda nota", "Categoría 2"));
        lista.add(new ItemClass("2", "Tercera Nota", "Mi Tercera nota", "Categoría 3"));
    }

    private void DeleteItem(View view, String ID) {
        int id = Integer.parseInt(ID);
        lista.remove(id);
        adapter.notifyDataSetChanged();
        showSnackbar(view, "Nota " + (id + 1) + " eliminada.");
    }

}
