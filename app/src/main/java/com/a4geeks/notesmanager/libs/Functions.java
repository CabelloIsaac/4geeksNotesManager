package com.a4geeks.notesmanager.libs;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Clase contentiva de funciones presentes en el proyecto
 */

public class Functions {

    //SnackBar function that shows messages
    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();
    }

}
