package com.a4geeks.notesmanager.listresources;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a4geeks.notesmanager.R;

import java.util.ArrayList;

/**
 * Esta clase es un adaptaador usado para llenar la lista de las categorías creadas por el usuario.
 *
 * Se encarga de obtener los datos del objeto CategoriasItem pasado a la clase y mostrarlos en pantalla
 * a través de unos TextView's.
 */

public class CategoriasItemAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<CategoriasItem> items;

    public CategoriasItemAdapter(Activity activity, ArrayList<CategoriasItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<CategoriasItem> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.categoria_row, null);
        }

        //Inicializando componentes
        TextView tvId = v.findViewById(R.id.tvId);
        TextView tvNombre = v.findViewById(R.id.tvNombre);

        //Guardando datos el elemento CategoriasItem (dir)
        CategoriasItem dir = items.get(position);

        //Obteniendo datos del elemento CategoriasItem (dir) y añadiéndolos a la interfaz
        tvId.setText(dir.getId());
        tvNombre.setText(dir.getNombre());

        return v;
    }
}
