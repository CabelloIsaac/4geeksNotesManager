package com.a4geeks.notesmanager;

import android.content.ComponentName;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.a4geeks.notesmanager.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.*;


/**
 * Created by cabel on 2/2/2018.
 */
public class LogInActivityTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityActivityTestRule = new ActivityTestRule<LogInActivity>(LogInActivity.class);

    private String email = "cabelloisaac@hotmail.com";
    private String password = "12345678";


    @Test
    public void onCreate() throws Exception {
        //Introduce el email en el campo de texto
        Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(typeText(email));
        //Cierra el teclado
        closeSoftKeyboard();
        //Introduce la contraseña
        onView(ViewMatchers.withId(R.id.etPassword)).perform(typeText(password));
        //Cierra el teclado
        closeSoftKeyboard();
        //Pulsa el botón de iniciar sesión
        onView(ViewMatchers.withId(R.id.btIniciarSesion)).perform(click());
        //Comprobar que se muestra el MainActivity
        intended(hasComponent(new ComponentName(getTargetContext(), MainActivity.class)));
    }

    @Test
    public void onActivityResult() throws Exception {
    }

    @Test
    public void onStart() throws Exception {
    }

    @Test
    public void signIn() throws Exception {
    }

    @Test
    public void signUp() throws Exception {
    }

}