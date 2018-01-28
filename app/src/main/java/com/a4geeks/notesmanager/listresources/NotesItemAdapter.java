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

import com.a4geeks.notesmanager.database.DBNotesManager;
import com.a4geeks.notesmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Adaptador para los Items de la lista de Categorías
 */

public class NotesItemAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<NotesItem> items;
    SQLiteDatabase db;

    public NotesItemAdapter(Activity activity, ArrayList<NotesItem> items) {
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

    public void addAll(ArrayList<NotesItem> category) {
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


        //Inicializando componentes
        TextView tvId = v.findViewById(R.id.tvId);
        final TextView tvTitulo = v.findViewById(R.id.tvTitulo);
        TextView tvDate = v.findViewById(R.id.tvDate);
        TextView tvCategoria = v.findViewById(R.id.tvCategoria);
        CheckBox checkCompleted = v.findViewById(R.id.checkCompletada);

        //Creando DataBase Object
        DBNotesManager dbNotesManager = new DBNotesManager(activity.getApplicationContext(), DBNotesManager.DB_NAME, null, 1);
        db = dbNotesManager.getWritableDatabase();

        NotesItem dir = items.get(position);

        //Guardando datos en variables
        final String id = dir.getId();
        final String titulo = dir.getTitulo();
        String categoria = getNombreCategoriaById(dir.getCategoria());
        String date = dir.getDate();
        String isCompleted = dir.getCompletada();

        //Convirtiendo UnixTime a un patrón humano
        long dv = Long.valueOf(date);
        Date df = new Date(dv);
        String finalDate = new SimpleDateFormat("dd/MM/yyyy - hh:mm a").format(df);

        //Aplicando datos a los componentes
        tvId.setText(id);
        tvTitulo.setText(dir.getTitulo());
        tvCategoria.setText(categoria);
        tvDate.setText(finalDate);

        //Comprueba si la nota está completada para aplicar el cambio visual
        if (isCompleted.equals("1")) {
            checkCompleted.setChecked(false);
            tvTitulo.setTextColor(Color.parseColor("#212121"));
        } else {
            checkCompleted.setChecked(true);
            tvTitulo.setPaintFlags(tvTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvTitulo.setTextColor(Color.parseColor("#757575"));
        }


        checkCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                      @Override
                                                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                          if (isChecked) {
                                                              db.execSQL("UPDATE notas SET completada = '0' WHERE id = '" + id + "'");
                                                              items.get(position).completada = "0";
                                                              tvTitulo.setPaintFlags(tvTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                              tvTitulo.setTextColor(Color.parseColor("#757575"));
                                                          } else {
                                                              db.execSQL("UPDATE notas SET completada = '1' WHERE id = '" + id + "'");
                                                              items.get(position).completada = "1";
                                                              tvTitulo.setPaintFlags(tvTitulo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                                              tvTitulo.setTextColor(Color.parseColor("#212121"));
                                                          }
                                                      }
                                                  }

        );

        return v;
    }

    private String getNombreCategoriaById(String id) {
        String nombre = "";

        //Solicita el nombre la categoria a la base de datos basándose en el ID
        Cursor c = db.rawQuery("SELECT nombre FROM " + DBNotesManager.CATEGORIA_TABLE_NAME + " WHERE id = '" + id + "'", null);

        if (c.moveToFirst()) {

            do {

                nombre = c.getString(0);

            } while (c.moveToNext());

        } else {
            nombre = "Sin categoría";
        }

        return nombre;
    }

}
