package com.a4geeks.notesmanager.listresources;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.a4geeks.notesmanager.R;

/**
 * Esta clase es un adaptador usado para llenar la lista de las categorías creadas por el usuario
 * que se mostrarán en una lista desplegable al crear una nueva nota.
 *
 * Se encarga de obtener los datos del objeto Cursor pasado a la clase y mostrarlos en pantalla
 * a través de unos TextView's.
 */


public class CategoriasSpinnerItemAdapter extends CursorAdapter {

    /**
     * Constructor de la clase CategoriasSpinnerItemAdapter
     */
    public CategoriasSpinnerItemAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.categoria_item_spinner, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Referencias UI.
        TextView tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        TextView tvId = (TextView) view.findViewById(R.id.tvId);

        // Get valores.
        String id = cursor.getString(0);
        String nombre = cursor.getString(1);

        // Setup
        tvId.setText(id);
        tvNombre.setText(nombre);

    }

}