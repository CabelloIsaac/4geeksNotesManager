package com.a4geeks.notesmanager.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.a4geeks.notesmanager.LogInActivity;
import com.a4geeks.notesmanager.R;
import com.a4geeks.notesmanager.libs.Constantes;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/** Activity principal que muestra el listado de Notas al ejecutarse.
 * Contiene, además, el menú lateral izquierdo para mostrar la lista de Categorías.
 * En el menú superior derecho, se le permite al usuario modificar el orden en que se muestra
 * las notas**/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NotasFragment.OnFragmentInteractionListener,
        CategoriasFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    NavigationView navigationView;

    SharedPreferences sharedPreferences;

    int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(Constantes.MyPREFERENCES, Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_inicio, 0);
            setTitle("Notas");
        }
    }

    /**
     * Se llama a este método a penas se inicia la clase. Comprueba si existe una sesión iniciada.
     * Si no existe, va a la pantalla de iniciar sesión.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Si no existe una sesión activa, va a Iniciar Sesión
        if (currentUser == null) {
            goToLogIn();
        } else {
            navigationView.getMenu().performIdentifierAction(R.id.nav_inicio, 0);
            setTitle("Notas");
        }
    }

    /**
     * Va a la pantalla de inicio de sesión
     */
    private void goToLogIn() {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Se ejecuta si se pulsa el botón Back del dispositivo
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (currentFragment == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuOrdenarAZ) {

            //Ordena las notas por Nombre (A-Z)
            editor.putString("order_by", "az");
            editor.commit();
            Toast.makeText(this, "A-Z", Toast.LENGTH_SHORT).show();
            navigationView.getMenu().performIdentifierAction(R.id.nav_inicio, 0);
            setTitle("Notas");

            return true;
        } else if (id == R.id.menuOrdenarZA) {

            //Ordena las notas por Nombre (Z-A)
            editor.putString("order_by", "za");
            editor.commit();
            Toast.makeText(this, "za", Toast.LENGTH_SHORT).show();
            navigationView.getMenu().performIdentifierAction(R.id.nav_inicio, 0);

            return true;
        } else if (id == R.id.menuOrdenarCat) {

            //Ordena las notas por Categoría
            editor.putString("order_by", "cat");
            editor.commit();
            navigationView.getMenu().performIdentifierAction(R.id.nav_inicio, 0);
            Toast.makeText(this, "cat", Toast.LENGTH_SHORT).show();

            return true;
        } else if (id == R.id.menuOrdenarDateA) {

            //Ordena las notas por Fecha (Más antiguos primero)
            editor.putString("order_by", "dateA");
            editor.commit();
            navigationView.getMenu().performIdentifierAction(R.id.nav_inicio, 0);
            Toast.makeText(this, "date", Toast.LENGTH_SHORT).show();

            return true;
        } else if (id == R.id.menuOrdenarDateB) {

            //Ordena las notas por Fecha (Más nuevos primero)
            editor.putString("order_by", "dateB");
            editor.commit();
            navigationView.getMenu().performIdentifierAction(R.id.nav_inicio, 0);
            Toast.makeText(this, "date", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean FragmentTransaction = false;
        Fragment fragment = null;

        if (id == R.id.nav_inicio) {

            currentFragment = 0;
            fragment = new NotasFragment();
            FragmentTransaction = true;
            invalidateOptionsMenu();

        } else if (id == R.id.nav_categorias) {

            currentFragment = 1;
            fragment = new CategoriasFragment();
            FragmentTransaction = true;
            invalidateOptionsMenu();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            //Go to SignUpActivity Activity
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }

        if (FragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
