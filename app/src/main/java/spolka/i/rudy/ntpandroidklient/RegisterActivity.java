package spolka.i.rudy.ntpandroidklient;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etRegisterName = findViewById(R.id.etRegisterName);
        final EditText etRegister2ndName = findViewById(R.id.etRegister2ndName);
        final EditText etRegisterLogin = findViewById(R.id.etRegisterLogin);                                //pobranie wartosci z pol
        final EditText etRegisterPassword = findViewById(R.id.etRegisterPassword);
        final EditText etRegisterEmail = findViewById(R.id.etRegisterEmail);
        final Button btnRegisterRegister = findViewById(R.id.btnRegisterRegister);

        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etRegisterName.getText().toString();
                final String secondname = etRegister2ndName.getText().toString();                           //przepisanie tych wartosci do stringow zeby je wyslac w requescie
                final String login = etRegisterLogin.getText().toString();
                final String password = etRegisterPassword.getText().toString();
                final String email = etRegisterEmail.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){                                // nasluch odpowiedzi z bazy

                        try {
                            JSONObject jsonResponse = new JSONObject(response);                             //rozpakowanie odpowiedzi
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);     //jesli tak to wraca do logoawnia
                                RegisterActivity.this.startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);       //jesli nie to blad wypiedala
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();                                                                               //wyjatek jak przyjdzie null
                        }

                    }
                };

                dbRegisterRequest registerRequest = new dbRegisterRequest(name,secondname,login,password,email,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);                                                 //dodanie do kolejki
                queue.add(registerRequest);
            }

            });
    }
}
