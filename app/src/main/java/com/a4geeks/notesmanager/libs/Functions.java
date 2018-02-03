package com.a4geeks.notesmanager.libs;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * En esta clase se tienen algunas funciones que pueden ser usadas en toda la aplicación con el
 * objetivo de ahorrar código.
 */

public class Functions {

    /**
     * A este método se le llama para mostrar un mensaje del estilo SnackBar en la pantalla.
     * @param view vista en la que se mostrará el mensaje
     * @param message mensaje a mostrar
     */
    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();
    }

}
