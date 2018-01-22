package com.a4geeks.notesmanager.Main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.a4geeks.notesmanager.DetailNotes.DetailActivity;
import com.a4geeks.notesmanager.R;

import java.util.ArrayList;

import static com.a4geeks.notesmanager.libs.Functions.showSnackbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

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

        lvLista = v.findViewById(R.id.lvLista);
        lvLista.setLongClickable(true);
        LlenarLista();

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
                intent.putExtra("titulo", Titulo);
                intent.putExtra("descripcion", Descripcion);
                intent.putExtra("categoria", Categoria);
                startActivity(intent);

            }
        });

        lvLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int position, long l) {

                final String ID = ((TextView) view.findViewById(R.id.tvId)).getText().toString();

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getContext()).create();

                // Setting Dialog Title
                alertDialog.setTitle("Eliminar nota");
                // Setting Dialog Message
                alertDialog.setMessage("¿Desea eliminar esta nota?");
                // Setting OK Button
                alertDialog.setButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        DeleteItem(view, ID);
                    }
                });

                // Setting Cancel Button
                alertDialog.setButton2("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                return true;
            }


        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        return v;
    }

    private void LlenarLista() {
        lista.add(new ItemClass("0", "Primera Nota", "Mi Primera nota", "Importante"));
        lista.add(new ItemClass("1", "Segunda Nota", "Mi Segunda nota", "Cocina"));
        lista.add(new ItemClass("2", "Tercera Nota", "Mi Tercera nota", "Otros"));
    }

    private void DeleteItem(View view, String ID) {
        int id = Integer.parseInt(ID);
        lista.remove(id);
        adapter.notifyDataSetChanged();
        showSnackbar(view, "Nota " + (id + 1) + " eliminada.");
    }



    public interface OnFragmentInteractionListener {
    }
}
