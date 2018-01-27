package com.a4geeks.notesmanager.main;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.a4geeks.notesmanager.addeditnotes.AddEditActivity;
import com.a4geeks.notesmanager.database.dbNotesManager;
import com.a4geeks.notesmanager.detailnotes.DetailActivity;
import com.a4geeks.notesmanager.listresources.ItemAdapter;
import com.a4geeks.notesmanager.listresources.ItemClass;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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
    SharedPreferences sharedPreferences;
    EditText etBuscar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Constantes.MyPREFERENCES, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        dbNotesManager formulario = new dbNotesManager(getContext(), dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        lvLista = v.findViewById(R.id.lvLista);
        etBuscar = v.findViewById(R.id.etBuscar);

        adapter = new ItemAdapter(getActivity(), lista);

        lvLista.setAdapter(adapter);

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ID = ((TextView) view.findViewById(R.id.tvId)).getText().toString();

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(dbNotesManager.ID, ID);
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

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Buscar(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        LlenarLista();

        return v;
    }

    private void LlenarLista() {

        if (mAuth.getCurrentUser() != null) {

            String usuario = mAuth.getCurrentUser().getUid();
            Cursor c = null;
            String orderBy = sharedPreferences.getString("order_by", "date");

            if (orderBy.equals("az")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY titulo ASC", null);
            } else if (orderBy.equals("za")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY titulo DESC", null);
            } else if (orderBy.equals("cat")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY id_categoria ASC", null);
            } else if (orderBy.equals("dateA")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY date DESC", null);
            } else if (orderBy.equals("dateB")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY date ASC", null);
            } else {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY date DESC", null);
            }

            if (c.moveToFirst()) {

                lista.clear();

                do {

                    String ID = c.getString(0);
                    String CATEGORIA = c.getString(1);
                    String TITULO = c.getString(3);
                    String DESCRIPCION = c.getString(4);
                    String COMPLETADA = c.getString(5);
                    String DATE = c.getString(6);

                    lista.add(new ItemClass(ID, CATEGORIA, TITULO, DESCRIPCION, COMPLETADA, DATE));
                    adapter.notifyDataSetChanged();

                } while (c.moveToNext());

            }
        }
    }

    private void Buscar(String titulo) {

        if (mAuth.getCurrentUser() != null) {

            String usuario = mAuth.getCurrentUser().getUid();
            Cursor c = null;
            String orderBy = sharedPreferences.getString("order_by", "date");


            if (orderBy.equals("az")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + titulo + "%') ORDER BY titulo ASC", null);
            } else if (orderBy.equals("za")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + titulo + "%') ORDER BY titulo DESC", null);
            } else if (orderBy.equals("cat")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + titulo + "%') ORDER BY id_categoria ASC", null);
            } else if (orderBy.equals("dateA")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + titulo + "%') ORDER BY date DESC", null);
            } else if (orderBy.equals("dateB")) {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + titulo + "%') ORDER BY date ASC", null);
            } else {
                c = db.rawQuery("SELECT * FROM " + dbNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' AND titulo LIKE '%" + titulo + "%' ORDER BY date DESC", null);
            }

            if (c.moveToFirst()) {

                lista.clear();

                do {

                    String ID = c.getString(0);
                    String CATEGORIA = c.getString(1);
                    String TITULO = c.getString(3);
                    String DESCRIPCION = c.getString(4);
                    String COMPLETADA = c.getString(5);
                    String DATE = c.getString(6);

                    lista.add(new ItemClass(ID, CATEGORIA, TITULO, DESCRIPCION, COMPLETADA, DATE));
                    adapter.notifyDataSetChanged();

                } while (c.moveToNext());

            } else {
                lista.clear();
                adapter.notifyDataSetChanged();

            }
        }
    }

    public interface OnFragmentInteractionListener {
    }

}