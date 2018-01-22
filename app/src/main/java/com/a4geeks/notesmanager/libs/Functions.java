package com.a4geeks.notesmanager.libs;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by cabel on 21/1/2018.
 */

public class Functions {

    //SnackBar function that shows messages
    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();
    }

}
