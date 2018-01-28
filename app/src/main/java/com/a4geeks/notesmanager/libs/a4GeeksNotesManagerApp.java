package com.a4geeks.notesmanager.libs;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Clase de tipo Application necesario para la configuración del SDK de Facebook
 */

public class a4GeeksNotesManagerApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

}
