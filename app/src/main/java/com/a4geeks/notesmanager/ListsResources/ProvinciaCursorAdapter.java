package com.a4geeks.notesmanager.ListsResources;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.a4geeks.notesmanager.R;

/**
 * Created by cabel on 17/8/2017.
 */

public class ProvinciaCursorAdapter extends CursorAdapter {

    public ProvinciaCursorAdapter(Context context, Cursor c) {
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
        String ID = cursor.getString(0);
        String PRO_NOMBRE = cursor.getString(1);

        // Setup
        tvId.setText(ID);
        tvNombre.setText(PRO_NOMBRE);

    }

}