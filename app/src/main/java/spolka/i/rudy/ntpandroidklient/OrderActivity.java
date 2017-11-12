package spolka.i.rudy.ntpandroidklient;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {

    SessionManager currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        final Button btnOrderZlozZamow = findViewById(R.id.btnOrderZlozZamow);

        final EditText etOrderCuk1 = findViewById(R.id.etOrderCuk1);
        final Button btnOrderCuk1Plus = findViewById(R.id.btnOrderCuk1Plus);
        final Button btnOrderCuk1Minus = findViewById(R.id.btnOrderCuk1Minus);

        final EditText etOrderCuk2 = findViewById(R.id.etOrderCuk2);
        final Button btnOrderCuk2Plus = findViewById(R.id.btnOrderCuk2Plus);
        final Button btnOrderCuk2Minus = findViewById(R.id.btnOrderCuk2Minus);

        final EditText etOrderCuk3 = findViewById(R.id.etOrderCuk3);
        final Button btnOrderCuk3Plus = findViewById(R.id.btnOrderCuk3Plus);
        final Button btnOrderCuk3Minus = findViewById(R.id.btnOrderCuk3Minus);

        final TextView etOrderUserId = findViewById(R.id.etOrderUserID);

        currentSession = new SessionManager(OrderActivity.this);
        HashMap<String, String> user = currentSession.getUserDetails();
        String userID = user.get(SessionManager.KEY_USER_ID);
        etOrderUserId.setText(userID);

        btnOrderCuk1Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etOrderCuk1.getText().toString();
                int i = Integer.parseInt(s);
                etOrderCuk1.setText(String.valueOf(i+1));
            }
        });

        btnOrderCuk1Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etOrderCuk1.getText().toString();
                int i = Integer.parseInt(s);
                etOrderCuk1.setText(String.valueOf(i-1));
            }
        });

        btnOrderCuk2Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etOrderCuk2.getText().toString();
                int i = Integer.parseInt(s);
                etOrderCuk2.setText(String.valueOf(i+1));
            }
        });

        btnOrderCuk2Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etOrderCuk2.getText().toString();
                int i = Integer.parseInt(s);
                etOrderCuk2.setText(String.valueOf(i-1));
            }
        });

        btnOrderCuk3Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etOrderCuk3.getText().toString();
                int i = Integer.parseInt(s);
                etOrderCuk3.setText(String.valueOf(i+1));
            }
        });

        btnOrderCuk3Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etOrderCuk3.getText().toString();
                int i = Integer.parseInt(s);
                etOrderCuk3.setText(String.valueOf(i-1));
            }
        });

        btnOrderZlozZamow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int prodCount1 = Integer.valueOf(etOrderCuk1.getText().toString());
                final int prodCount2 = Integer.valueOf(etOrderCuk2.getText().toString());
                final int prodCount3 = Integer.valueOf(etOrderCuk3.getText().toString());

                final Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success)
                                Log.e("TAG","OK");
                            else
                                Log.e("TAG","FAIL");
                            if(success) {
                                AlertDialog.Builder popupSuccess = new AlertDialog.Builder((OrderActivity.this));
                                popupSuccess.setMessage("Zamowienie zostalo zlozone")
                                        .setNegativeButton("OK",null)
                                        .create()
                                        .show();//jeszce nie wiem
                            }else{
                                AlertDialog.Builder popupFail = new AlertDialog.Builder(OrderActivity.this);       //jesli nie to blad wypiedala
                                popupFail.setMessage("BLAD, zmowienie nie zostalo zlozone")
                                        .setNegativeButton("OK",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };



                dbOrderRequest orderRequest = new dbOrderRequest(10, prodCount1, prodCount2, prodCount3, responseListener);
                RequestQueue queue = Volley.newRequestQueue(OrderActivity.this);                                    //dodanie requestu do kolejki
                queue.add(orderRequest);
            }

        });
    }
}