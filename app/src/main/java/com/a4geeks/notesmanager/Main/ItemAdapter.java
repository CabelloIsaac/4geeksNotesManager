package com.a4geeks.notesmanager.Main;

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
 * Created by cabel on 21/1/2018.
 */

public class ItemAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ItemClass> items;

    public ItemAdapter(Activity activity, ArrayList<ItemClass> items) {
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

    public void addAll(ArrayList<ItemClass> category) {
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
            v = inf.inflate(R.layout.item_row, null);
        }

        ItemClass dir = items.get(position);

        TextView id = v.findViewById(R.id.tvId);
        id.setText(dir.getId());

        TextView titulo = v.findViewById(R.id.tvTitulo);
        titulo.setText(dir.getTitulo());

        TextView descripcion = v.findViewById(R.id.tvDescripcion);
        descripcion.setText(dir.getDescripcion());

        TextView categoria = v.findViewById(R.id.tvCategoria);
        categoria.setText(dir.getCategoria());

        return v;
    }
}
