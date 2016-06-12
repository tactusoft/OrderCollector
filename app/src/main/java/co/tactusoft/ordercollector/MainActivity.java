package co.tactusoft.ordercollector;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.entities.Usuario;
import co.tactusoft.ordercollector.fragments.FragmentBodegas;
import co.tactusoft.ordercollector.fragments.FragmentHome;
import co.tactusoft.ordercollector.fragments.FragmentOrdenesEntrada;
import co.tactusoft.ordercollector.fragments.FragmentOrdenesEntradaDetalle;
import co.tactusoft.ordercollector.fragments.FragmentVehiculo;
import co.tactusoft.ordercollector.util.DataBaseHelper;
import co.tactusoft.ordercollector.util.Singleton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        try {
            dataBaseHelper.createDataBase();
            Usuario usuario = dataBaseHelper.getUsuario();
            if(usuario != null) {
                Singleton.getInstance().setUsuario(usuario);
                OrdenesEntradas ordenesEntradas = dataBaseHelper.geOrdenesEntradas();
                Singleton.getInstance().setOrdenesEntradas(ordenesEntradas);
            } else {
                usuario = new Usuario();
                usuario.setUsuarioId(1);
                usuario.setNombreUsuario("carlossarmientor@gmail.com");
                Singleton.getInstance().setUsuario(usuario);
                dataBaseHelper.insertUsuario(usuario);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showFragment(new FragmentHome(), R.string.navigation_item_home);
    }

    public void showFragment(Fragment fragment, int title) {
        getSupportActionBar().setTitle(title);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            for (Fragment frag : fm.getFragments()) {
                if (frag!=null && frag.isVisible()) {
                    if (frag instanceof FragmentOrdenesEntradaDetalle) {
                        showFragment(new FragmentOrdenesEntrada(), R.string.navigation_item_oe);
                        return;
                    } else if (frag instanceof FragmentOrdenesEntrada) {
                        showFragment(new FragmentHome(), R.string.navigation_item_home);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        return;
                    } else if (frag instanceof FragmentVehiculo) {
                        showFragment(new FragmentOrdenesEntradaDetalle(), R.string.navigation_item_home);
                        navigationView.getMenu().getItem(2).setChecked(true);
                        return;
                    }
                }
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Integer title = R.string.navigation_item_home;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            title = R.string.navigation_item_home;
            fragment = new FragmentHome();
        } else if (id == R.id.nav_bodegas) {
            title = R.string.navigation_item_bodegas;
            fragment = new FragmentBodegas();
        } else if (id == R.id.nav_oe) {
            title = R.string.navigation_item_oe;
            fragment = new FragmentOrdenesEntrada();
        } else if (id == R.id.nav_os) {
            title = R.string.navigation_item_os;
        } else if (id == R.id.nav_close_session) {

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }
}
