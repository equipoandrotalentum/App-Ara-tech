package android.ara_tech.es.aratech;



import android.ara_tech.es.aratech.Fragments.CuidadorFragment;

import android.ara_tech.es.aratech.Fragments.MapFragment;
import android.ara_tech.es.aratech.Fragments.PerfilFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, //OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        CuidadorFragment.OnFragmentInteractionListener,
        PerfilFragment.OnFragmentInteractionListener{

    //Sacámos el Fragmento de mapa como global.
    private MapFragment tMap;
    ImageView imgUser;
    Button btEditar, btLogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //--------------NAV HEADER MAIN -------------------------------

        View view = navigationView.getHeaderView(0);
        imgUser = (ImageView)view.findViewById(R.id.imageView);
        btEditar = (Button)view.findViewById(R.id.btEditar);
        btLogout = (Button)view.findViewById(R.id.btCerrar);
        TextView tvUsuario = (TextView)view.findViewById(R.id.usuarioHeader);
        TextView tvEmail = (TextView)view.findViewById(R.id.tvEmailHeader);
        if(ParseUser.getCurrentUser()!=null) {
            tvUsuario.setText(ParseUser.getCurrentUser().getUsername());
            tvEmail.setText(ParseUser.getCurrentUser().getEmail());
        }
        //Hacemos uso del FragmentManager para cambiar de fragmento principal y
        //establecer el mapa.
        FragmentManager fm = getSupportFragmentManager();
        MapFragment testMap = MapFragment.newInstance();
        fm.beginTransaction().replace(R.id.content_frame, testMap).addToBackStack(null).commit();
        tMap=testMap;

        //---------------------Listener imagen perfil------------------------
        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace( R.id.content_frame, new PerfilFragment(), "Editar" ).addToBackStack(null);
                transaction.commit();
                drawer.closeDrawers();
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logOut();
                if (TwitterCore.getInstance().getSessionManager().getActiveSession()!=null){
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                }
            /*LoginActivity lA = new LoginActivity();
            lA.signOutGoogle();*/
             Toast.makeText(MainActivity.this, "Cerrada la sesión", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //-----------------PROGRAMAMOS AQUÏ LAS ACCIONES DEL MENÚ DE ARRIBA-------------------------
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




    //--------------ACCIONES DEL MENU LATERAL----------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.home) {
            Log.i("Navigation","Pulsado menú camara");
            //MapFragment fragment = tMap;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, tMap).addToBackStack(null).commit();
            //tMap.getMapAsync(this);

        } else if (id == R.id.zonas) {
            Intent intent = new Intent(this,ZonasActivity.class);
            startActivity(intent);
        } else if (id == R.id.cuidadores) {

        } else if (id == R.id.notificaciones) {

        } else if (id == R.id.chat) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
