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
import com.a4geeks.notesmanager.database.DBNotesManager;
import com.a4geeks.notesmanager.detailnotes.DetailActivity;
import com.a4geeks.notesmanager.listresources.NotesItemAdapter;
import com.a4geeks.notesmanager.listresources.NotesItem;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Esta clase mostrará todas las notas que el usuario actual haya registrado en la aplicación.
 * Las notas se ordenarán como el usuario haya indicado en el menú superior derecho de la pantalla.
 * También permitirá al usuario buscar entre sus notas indicando una palabra clave en la barra de búsqueda.
 */

public class NotasFragment extends Fragment {

    private FirebaseAuth mAuth;
    private SQLiteDatabase db;

    private OnFragmentInteractionListener mListener;

    public NotasFragment() {
        // Required empty public constructor
    }

    private ArrayList<NotesItem> lista = new ArrayList<NotesItem>();
    private NotesItemAdapter adapter;
    private ListView lvLista;
    private SharedPreferences sharedPreferences;
    private EditText etBuscar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Constantes.MyPREFERENCES, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        DBNotesManager formulario = new DBNotesManager(getContext(), DBNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        lvLista = v.findViewById(R.id.lvLista);
        etBuscar = v.findViewById(R.id.etBuscar);

        adapter = new NotesItemAdapter(getActivity(), lista);

        lvLista.setAdapter(adapter);

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemId = ((TextView) view.findViewById(R.id.tvId)).getText().toString();

                Intent i = new Intent(getContext(), DetailActivity.class);
                i.putExtra(DBNotesManager.ID, itemId);
                startActivity(i);

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
                buscar(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        llenarLista();

        return v;
    }

    /**
     * Obtiene todas las notas creadas por el usuario loggeado y las muestra en una lista en pantalla
     * El usuario puede configurar con el menú superior derecho el orden en el que se mostrarán las notas.
     * El usuario podrá hacer clic sobre ellas para verlas en detalle y luego modificarlas o eliminarlas.
     *
     */
    private void llenarLista() {

        if (mAuth.getCurrentUser() != null) {

            String usuario = mAuth.getCurrentUser().getUid();
            Cursor cursor = null;
            String orderBy = sharedPreferences.getString("order_by", "date");

            if (orderBy.equals("az")) {
                cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY titulo ASC", null);
            } else if (orderBy.equals("za")) {
                cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY titulo DESC", null);
            } else if (orderBy.equals("cat")) {
                cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY id_categoria ASC", null);
            } else if (orderBy.equals("dateA")) {
                cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY date DESC", null);
            } else if (orderBy.equals("dateB")) {
                cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY date ASC", null);
            } else {
                cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' ORDER BY date DESC", null);
            }

            if (cursor.moveToFirst()) {

                lista.clear();

                do {

                    String id = cursor.getString(0);
                    String categoria = cursor.getString(1);
                    String titulo = cursor.getString(3);
                    String descripcion = cursor.getString(4);
                    String completada = cursor.getString(5);
                    String date = cursor.getString(6);

                    lista.add(new NotesItem(id, categoria, titulo, descripcion, completada, date));
                    adapter.notifyDataSetChanged();

                } while (cursor.moveToNext());

            }
        }
    }

    /**
     * Filtra las notas creadas por el usuario en las que su título contenga la palabra clave indicada por el usuario
     * y devuelve una lista ordenada de la forma que el usuario haya indicado.
     *
     * @param filtro palabra clave que el usuario desea buscar
     */
    private void buscar(String filtro) {

        String usuario = mAuth.getCurrentUser().getUid();
        Cursor cursor = null;

        //Obtiene el método de ordenación registrado por el usuario
        String orderBy = sharedPreferences.getString("order_by", "date");

        //Basado en el método de ordenación, realiza la búsqueda de notas pertenecientes al usuario y al filtro de búsqueda
        if (orderBy.equals("az")) {
            cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + filtro + "%') ORDER BY titulo ASC", null);
        } else if (orderBy.equals("za")) {
            cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + filtro + "%') ORDER BY titulo DESC", null);
        } else if (orderBy.equals("cat")) {
            cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + filtro + "%') ORDER BY id_categoria ASC", null);
        } else if (orderBy.equals("dateA")) {
            cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + filtro + "%') ORDER BY date DESC", null);
        } else if (orderBy.equals("dateB")) {
            cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE (id_usuario = '" + usuario + "') AND (titulo LIKE '%" + filtro + "%') ORDER BY date ASC", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM " + DBNotesManager.NOTAS_TABLE_NAME + " WHERE id_usuario = '" + usuario + "' AND titulo LIKE '%" + filtro + "%' ORDER BY date DESC", null);
        }

        //Si encuentra valores, los añade a la lista
        if (cursor.moveToFirst()) {

            lista.clear();

            do {

                String id = cursor.getString(0);
                String categoria = cursor.getString(1);
                String titulo = cursor.getString(3);
                String descripcion = cursor.getString(4);
                String completada = cursor.getString(5);
                String date = cursor.getString(6);

                lista.add(new NotesItem(id, categoria, titulo, descripcion, completada, date));
                adapter.notifyDataSetChanged();

            } while (cursor.moveToNext());

        } else {
            lista.clear();
            adapter.notifyDataSetChanged();

        }
    }

    public interface OnFragmentInteractionListener {
    }

}