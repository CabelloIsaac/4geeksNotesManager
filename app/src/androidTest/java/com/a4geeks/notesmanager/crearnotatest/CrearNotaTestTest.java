package com.a4geeks.notesmanager.crearnotatest;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.a4geeks.notesmanager.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by cabel on 2/2/2018.
 */
public class CrearNotaTestTest {

    @Rule
    public ActivityTestRule<CrearNotaTest> mActivityActivityTestRule = new ActivityTestRule<CrearNotaTest>(CrearNotaTest.class);

    private String titulo = "Prueba titulo nota";
    private String categoria = "Prueba categoria nota";
    private String descripcion = "Prueba descripcion nota";

    @Test
    public void onCreate() throws Exception {

        //Introduce el email en el campo de texto
        onView(ViewMatchers.withId(R.id.etTitulo)).perform(typeText(titulo));
            //Cierra el teclado
        closeSoftKeyboard();
        //Introduce la contraseña
        onView(ViewMatchers.withId(R.id.etCategoria)).perform(typeText(categoria));
         //Cierra el teclado
        closeSoftKeyboard();
        //Confirma la contraseña
        onView(ViewMatchers.withId(R.id.etDescripcion)).perform(typeText(descripcion));
            //Cierra el teclado
        closeSoftKeyboard();
        //Pulsa el botón de iniciar sesión
        onView(ViewMatchers.withId(R.id.btGuardar)).perform(click());

        onView(ViewMatchers.withId(R.id.tvResultado)).check(matches(withText("Completado")));

    }

}