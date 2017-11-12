package spolka.i.rudy.ntpandroidklient;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
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

public class RegisterActivity extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        final EditText etRegisterName = getView().findViewById(R.id.etRegisterName);
        final EditText etRegister2ndName = getView().findViewById(R.id.etRegister2ndName);
        final EditText etRegisterLogin = getView().findViewById(R.id.etRegisterLogin);                                //pobranie wartosci z pol
        final EditText etRegisterPassword = getView().findViewById(R.id.etRegisterPassword);
        final EditText etRegisterPasswordConfirm = getView().findViewById(R.id.etRegisterPasswordConfirm);
        final EditText etRegisterEmail = getView().findViewById(R.id.etRegisterEmail);
        final Button btnRegisterRegister = getView().findViewById(R.id.btnRegisterRegister);

        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etRegisterName.getText().toString();
                final String secondname = etRegister2ndName.getText().toString();                           //przepisanie tych wartosci do stringow zeby je wyslac w requescie
                final String login = etRegisterLogin.getText().toString();
                final String password = etRegisterPassword.getText().toString();
                final String passwordConfirm = etRegisterPasswordConfirm.getText().toString();
                final String email = etRegisterEmail.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){                                // nasluch odpowiedzi z bazy

                        try {
                            JSONObject jsonResponse = new JSONObject(response);                             //rozpakowanie odpowiedzi
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {
                                Toast tost = Toast.makeText(getActivity(),"Zarejestrowano pomyslnie",Toast.LENGTH_SHORT);
                                tost.show();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());       //jesli nie to blad wypiedala
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

                final boolean passwordMatch = password.equals(passwordConfirm);
                if (passwordMatch) {
                    dbRegisterRequest registerRequest = new dbRegisterRequest(name, secondname, login, password, email, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());                                                 //dodanie do kolejki
                    queue.add(registerRequest);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());       //jesli nie to blad wypiedala
                    builder.setMessage("Podane hasla nie sa takie same")
                            .setNeutralButton("OK", null)
                            .create()
                            .show();
                }
            }

            });
    }
}
