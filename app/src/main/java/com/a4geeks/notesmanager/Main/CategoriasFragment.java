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
import com.a4geeks.notesmanager.database.dbNotesManager;
import com.a4geeks.notesmanager.listresources.CategoriasAdapter;
import com.a4geeks.notesmanager.listresources.CategoriasClass;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriasFragment extends Fragment {

    private FirebaseAuth mAuth;
    SQLiteDatabase db;

    private ListFragment.OnFragmentInteractionListener mListener;

    ArrayList<CategoriasClass> lista = new ArrayList<CategoriasClass>();
    CategoriasAdapter adapter;
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

        dbNotesManager formulario = new dbNotesManager(getContext(), dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        lvLista = v.findViewById(R.id.lvLista);
        adapter = new CategoriasAdapter(getActivity(), lista);

        lvLista.setAdapter(adapter);

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ID = ((TextView) view.findViewById(R.id.tvId)).getText().toString();

                Intent intent = new Intent(getContext(), AddEditCategoriasActivity.class);
                intent.putExtra(Constantes.ADD_EDIT_ACTION, 1);
                intent.putExtra(dbNotesManager.ID, ID);
                startActivity(intent);

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

        LlenarLista();

        return v;
    }

    private void LlenarLista() {

        if (mAuth.getCurrentUser() != null) {

            String usuario = mAuth.getCurrentUser().getUid();

            Cursor c = db.rawQuery("SELECT * FROM " + dbNotesManager.CATEGORIA_TABLE_NAME + " WHERE id_usuario = '" + usuario + "'", null);
            if (c.moveToFirst()) {
                do {
                    String ID = c.getString(0);
                    String NOMBRE = c.getString(2);
                    lista.add(new CategoriasClass(ID, NOMBRE));
                } while (c.moveToNext());

            }
        }
    }

    public interface OnFragmentInteractionListener {
    }
}