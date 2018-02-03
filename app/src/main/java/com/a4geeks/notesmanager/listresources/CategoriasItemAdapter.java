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

    /**
     * Constructor de la clase CategoriasItemAdapter
     * @param activity activity que contiene el constructor
     * @param items arraylist con los items de la lista
     */
    public CategoriasItemAdapter(Activity activity, ArrayList<CategoriasItem> items) {
        this.activity = activity;
        this.items = items;
    }

    /**
     * Devuelve la cantidad de elementos de la lista
     * @return cantidad de elementos de la lista
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * Vacía la lista
     */
    public void clear() {
        items.clear();
    }

    /**
     * Vuelve a llenar la lista
     * @param category lista
     */
    public void addAll(ArrayList<CategoriasItem> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    /**
     * Obtiene un item de la lista
     * @param arg0 el id a obtener
     * @return el item de la lista
     */
    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    /**
     * Obtiene el id de un item de la lista
     * @param position posicion para obtener el id
     * @return el id del item de la lista
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Crea la vista final del item de la lista
     * @param position posicion del item a crear
     * @param convertView
     * @param parent
     * @return la vista creada
     */
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
