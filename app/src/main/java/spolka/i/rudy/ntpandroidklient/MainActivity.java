package spolka.i.rudy.ntpandroidklient;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager currentSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentSession = new SessionManager(getApplicationContext());


        //zaincjuj toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //zainicjij drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //wysun drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
              navigationView.setNavigationItemSelectedListener(this);
              View hView = navigationView.getHeaderView(0);
             final TextView loggedID = (TextView)hView.findViewById(R.id.loggedID);
             final TextView loggedLogin = (TextView)hView.findViewById(R.id.loggedLogin);
             final TextView loggedEmail = (TextView)hView.findViewById(R.id.loggedEmail);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000); // in milliseconds
                        runOnUiThread(new Runnable() { //it has to run on the ui thread  to update the Textview
                            @Override
                            public void run() {
                                if(currentSession.isLoggedIn()) {
                                    HashMap<String, String> userCred = currentSession.getUserDetails();
                                    loggedID.setText(userCred.get(SessionManager.KEY_USER_ID));
                                    loggedLogin.setText(userCred.get(SessionManager.KEY_NAME));
                                    loggedEmail.setText(userCred.get(SessionManager.KEY_EMAIL));
                                }else{
                                    loggedID.setText("Niezalogowany");
                                    loggedLogin.setText("Niezalogowany");
                                    loggedEmail.setText("Niezalogowany");
                                }


                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        //wyswietl fragment
        displaySelectedScreen(R.id.nav_menu1);




    }



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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void displaySelectedScreen(int itemId) {
        currentSession = new SessionManager(MainActivity.this);
        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_menu1:
                fragment = new LoginActivity();
                break;

            case R.id.nav_menu2:
                if(!currentSession.isLoggedIn()){
                    fragment = new RegisterActivity();
                }else{
                    fragment = new LoginActivity();
                }
                break;

            case R.id.nav_menu3:
                if(currentSession.isLoggedIn()){
                    fragment = new OrderActivity();
                }else{
                    Toast tost = Toast.makeText(MainActivity.this,"Aby zlozyc zamowienie, zaloguj sie",Toast.LENGTH_SHORT);
                    tost.show();
                    fragment = new LoginActivity();
                }
                break;

            case R.id.nav_menu4:
                if(currentSession.isLoggedIn()){
                    fragment = new OrderHistoryActivity();
                }else{
                    Toast tost = Toast.makeText(MainActivity.this,"Aby przegladac zamowienia, zaloguj sie",Toast.LENGTH_SHORT);
                    tost.show();
                    fragment = new LoginActivity();
                }
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentSession.logoutUser();
    }

}