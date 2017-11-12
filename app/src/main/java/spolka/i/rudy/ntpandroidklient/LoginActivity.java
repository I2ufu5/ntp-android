package spolka.i.rudy.ntpandroidklient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    SessionManager currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        currentSession = new SessionManager(LoginActivity.this);

        final EditText etLogin = findViewById(R.id.etLogin);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLoginRegister = findViewById(R.id.btnLoginRegister);
        final Button btnLoginLogin = findViewById(R.id.btnLoginLogin);


       if(currentSession.isLoggedIn()){
            HashMap<String,String> userCred = currentSession.getUserDetails();
            String username = userCred.get(SessionManager.KEY_USER_ID);

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Jestes zalogowany jako:" + username );

            builder.setNegativeButton("Pozostan\nzalogowany", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent cancelLogoutIntent = new Intent(LoginActivity.this, OrderActivity.class);
                    LoginActivity.this.startActivity(cancelLogoutIntent);
                }
            });

           builder.setPositiveButton("Wyloguj", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                    currentSession.logoutUser();
               }
           });

           builder.create()
                   .show();
        }

        btnLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);                   //listener do guzika rejestracja -> przenosi do rejestraji
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String login = etLogin.getText().toString();                                                             //zrzutka pol do zmiennych ktore ida pozniej z requestem
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {                              //oczekowanie na odpowiedz z bazy

                    @Override
                    public void onResponse(String response) {
                        // zabezpieczenie przed wyjatkiem jak null przyjdzie w odpowiedzi
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String login = jsonResponse.getString("login");
                            String email = jsonResponse.getString("email");
                            String userID = jsonResponse.getString("userID");

                            if (success) {
                                currentSession.createLoginSession(userID,login,email);
                                Toast tost = Toast.makeText(getApplicationContext(),"Zalogowano",Toast.LENGTH_SHORT);
                                tost.show();

                                Intent loginIntent = new Intent(LoginActivity.this, OrderActivity.class);
                                LoginActivity.this.startActivity(loginIntent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();                                                                          // wyplapuje wyjatki
                        }


                    }
                };

                if (password.length() <= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Haslo nie moze byc puste")
                            .setNeutralButton("OK", null)
                            .create()
                            .show();
                }else {
                    dbLoginRequest loginRequest = new dbLoginRequest(login, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);                                    //dodanie requestu do kolejki
                    queue.add(loginRequest);
                }
            }
        });
    }
}
