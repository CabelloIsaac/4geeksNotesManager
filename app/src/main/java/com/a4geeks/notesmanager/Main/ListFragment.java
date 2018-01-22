package com.a4geeks.notesmanager.Main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.a4geeks.notesmanager.AddEditNotes.AddEditActivity;
import com.a4geeks.notesmanager.DataBase.dbNotesManager;
import com.a4geeks.notesmanager.DetailNotes.DetailActivity;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.a4geeks.notesmanager.libs.Functions.showSnackbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private FirebaseAuth mAuth;
    SQLiteDatabase db;

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    ArrayList<ItemClass> lista = new ArrayList<ItemClass>();
    ItemAdapter adapter;
    ListView lvLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mAuth = FirebaseAuth.getInstance();

        dbNotesManager formulario = new dbNotesManager(getContext(), dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        lvLista = v.findViewById(R.id.lvLista);
        adapter = new ItemAdapter(getActivity(), lista);

        lvLista.setAdapter(adapter);


        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ID = ((TextView) view.findViewById(R.id.tvId)).getText().toString();
                String Titulo = ((TextView) view.findViewById(R.id.tvTitulo)).getText().toString();
                String Descripcion = ((TextView) view.findViewById(R.id.tvDescripcion)).getText().toString();
                String Categoria = ((TextView) view.findViewById(R.id.tvCategoria)).getText().toString();

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(dbNotesManager.ID, ID);
                intent.putExtra(dbNotesManager.NOTAS_TITULO, Titulo);
                intent.putExtra(dbNotesManager.NOTAS_DESCRIPCION, Descripcion);
                intent.putExtra(dbNotesManager.NOTAS_ID_CATEGORIA, Categoria);
                startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddEditActivity.class);
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

            Cursor c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "'", null);

            if (c.moveToFirst()) {

                while (c.moveToNext()) {
                    String ID = c.getString(0);
                    String CATEGORIA = c.getString(1);
                    String TITULO = c.getString(3);
                    String DESCRIPCION = c.getString(4);

                    lista.add(new ItemClass(ID, CATEGORIA, TITULO, DESCRIPCION));
                }

            }
        }
    }

    public interface OnFragmentInteractionListener {
    }

}