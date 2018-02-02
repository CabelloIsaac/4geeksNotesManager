package com.a4geeks.notesmanager.addeditnotes;

import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.widget.Toast;

import com.a4geeks.notesmanager.LogInActivity;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.*;

/**
 * Created by cabel on 2/2/2018.
 */
public class AddEditActivityTest {

    @Rule
    public ActivityTestRule<AddEditActivity> mActivityActivityTestRule = new ActivityTestRule<AddEditActivity>(AddEditActivity.class);

    private String titulo = "titulo";
    private String categoria = "Sin categoría";
    private String descripcion = "Descripción";
    FirebaseAuth mAuth;
    Context c;


    @Test
    public void onCreate() throws Exception {

    }

    @Before
    public void init() {
    }

}