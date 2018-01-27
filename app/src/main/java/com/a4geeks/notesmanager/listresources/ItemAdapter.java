package com.a4geeks.notesmanager.listresources;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.a4geeks.notesmanager.database.dbNotesManager;
import com.a4geeks.notesmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cabel on 21/1/2018.
 */

public class ItemAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ItemClass> items;
    SQLiteDatabase db;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_row, null);
        }

        ItemClass dir = items.get(position);

        final String idd = dir.getId();
        final String isCompleted = dir.getCompletada();

        CheckBox checkCompletada = v.findViewById(R.id.checkCompletada);

        dbNotesManager formulario = new dbNotesManager(activity.getApplicationContext(), dbNotesManager.DB_NAME, null, 1);
        db = formulario.getWritableDatabase();

        TextView id = v.findViewById(R.id.tvId);
        id.setText(dir.getId());

        final TextView titulo = v.findViewById(R.id.tvTitulo);
        titulo.setText(dir.getTitulo());

        TextView date = v.findViewById(R.id.tvDate);
        TextView categoria = v.findViewById(R.id.tvCategoria);

        //Converting UnixTime to Normal Date
        long dv = Long.valueOf(dir.getDate());
        Date df = new java.util.Date(dv);
        String finalDate = new SimpleDateFormat("dd/MM/yyyy - hh:mm a").format(df);

        date.setText(finalDate);

        Cursor c = db.rawQuery("SELECT nombre FROM " + dbNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + dir.getCategoria()+"'", null);

        if (c.moveToFirst()) {

            do {

                String NOMBRE = c.getString(0);

                categoria.setText(NOMBRE);

            } while (c.moveToNext());

        } else {
            categoria.setText("Sin categoría");
        }



        if (dir.getCompletada().equals("1")) {
            checkCompletada.setChecked(false);
            titulo.setTextColor(Color.parseColor("#212121"));
        } else {
            checkCompletada.setChecked(true);
            titulo.setPaintFlags(titulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            titulo.setTextColor(Color.parseColor("#757575"));
        }


        checkCompletada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked) {
                                                               db.execSQL("UPDATE notas SET completada = '0' WHERE id = '" + idd + "'");
                                                               items.get(position).completada = "0";
                                                               titulo.setPaintFlags(titulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                               titulo.setTextColor(Color.parseColor("#757575"));
                                                           } else {
                                                               db.execSQL("UPDATE notas SET completada = '1' WHERE id = '" + idd + "'");
                                                               items.get(position).completada = "1";
                                                               titulo.setPaintFlags(titulo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                                               titulo.setTextColor(Color.parseColor("#212121"));
                                                           }
                                                       }
                                                   }

        );

        return v;
    }
}
