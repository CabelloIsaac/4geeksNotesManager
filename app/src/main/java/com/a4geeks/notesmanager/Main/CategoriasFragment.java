package com.a4geeks.notesmanager.main;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.a4geeks.notesmanager.addeditcategorias.AddEditCategoriasActivity;
import com.a4geeks.notesmanager.database.DBNotesManager;
import com.a4geeks.notesmanager.listresources.CategoriasItemAdapter;
import com.a4geeks.notesmanager.listresources.CategoriasItem;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Fragment que se carga en el MainActivity que lista las categorías registradas por el usuario
 */

public class CategoriasFragment extends Fragment {

    private FirebaseAuth mAuth;
    SQLiteDatabase db;

    private NotasFragment.OnFragmentInteractionListener mListener;

    ArrayList<CategoriasItem> lista = new ArrayList<CategoriasItem>();
    CategoriasItemAdapter adapter;
    ListView lvLista;

    public CategoriasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categorias, container, false);

        mAuth = FirebaseAuth.getInstance();

        DBNotesManager dbNotesManager = new DBNotesManager(getContext(), DBNotesManager.DB_NAME, null, 1);
        db = dbNotesManager.getWritableDatabase();

        lvLista = v.findViewById(R.id.lvLista);
        adapter = new CategoriasItemAdapter(getActivity(), lista);

        lvLista.setAdapter(adapter);

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemId = ((TextView) view.findViewById(R.id.tvId)).getText().toString();

                Intent i = new Intent(getContext(), AddEditCategoriasActivity.class);
                i.putExtra(Constantes.ADD_EDIT_ACTION, 1);
                i.putExtra(DBNotesManager.ID, itemId);
                startActivity(i);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddEditCategoriasActivity.class);
                i.putExtra(Constantes.ADD_EDIT_ACTION, 0);
                startActivity(i);
            }
        });

        llenarLista();

        return v;
    }

    private void llenarLista() {

        if (mAuth.getCurrentUser() != null) {

            String usuario = mAuth.getCurrentUser().getUid();

            Cursor c = db.rawQuery("SELECT * FROM " + DBNotesManager.CATEGORIA_TABLE_NAME + " WHERE id_usuario = '" + usuario + "'", null);
            if (c.moveToFirst()) {
                do {
                    String id = c.getString(0);
                    String nombre = c.getString(2);
                    lista.add(new CategoriasItem(id, nombre));
                } while (c.moveToNext());

            }
        }
    }

    public interface OnFragmentInteractionListener {
    }
}