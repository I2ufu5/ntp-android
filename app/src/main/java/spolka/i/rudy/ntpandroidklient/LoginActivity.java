package spolka.i.rudy.ntpandroidklient;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends Fragment {

    SessionManager currentSession;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_login, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        currentSession = new SessionManager(getActivity());

        final EditText etLogin = getView().findViewById(R.id.etLogin);
        final EditText etPassword = getView().findViewById(R.id.etPassword);
        final Button btnLoginLogin = getView().findViewById(R.id.btnLoginLogin);


       if(currentSession.isLoggedIn()){
            HashMap<String,String> userCred = currentSession.getUserDetails();
            String username = userCred.get(SessionManager.KEY_NAME);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Jestes zalogowany jako:" + username );

            builder.setNegativeButton("Pozostan\nzalogowany", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //dissmiss fragment
                }
            });

           builder.setPositiveButton("Wyloguj", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   currentSession.logoutUser();
                   Toast tost = Toast.makeText(getActivity(),"Wyczyszczono dane sesji",Toast.LENGTH_SHORT);
                   tost.show();
               }
           });

           builder.create()
                   .show();
        }


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
                                Toast tost = Toast.makeText(getActivity(),"Zalogowano",Toast.LENGTH_SHORT);
                                tost.show();

                                //dissmiss fragment

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Haslo nie moze byc puste")
                            .setNeutralButton("OK", null)
                            .create()
                            .show();
                }else {
                    dbLoginRequest loginRequest = new dbLoginRequest(login, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());                                    //dodanie requestu do kolejki
                    queue.add(loginRequest);
                }
            }
        });
    }
}
