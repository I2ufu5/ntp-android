package spolka.i.rudy.ntpandroidklient;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etLogin = findViewById(R.id.etLogin);
        final EditText etPassword = findViewById(R.id.etPassword);                                                          //pobranie wartosci z pol i przyciskow
        final Button btnLoginRegister = findViewById(R.id.btnLoginRegister);
        final Button btnLoginLogin = findViewById(R.id.btnLoginLogin);

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

                            if (success) {
                                Intent loginIntent = new Intent(LoginActivity.this, OrderActivity.class);       //jak zaloguje to przejdzie do zamawiania (orderavtivity)
                                //loginIntent.putExtra("login", login);
                                LoginActivity.this.startActivity(loginIntent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);          // jak nie zaloguje to wyswietli popup
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

                dbLoginRequest loginRequest = new dbLoginRequest(login, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);                                    //dodanie requestu do kolejki
                queue.add(loginRequest);

            }
        });
    }
}
