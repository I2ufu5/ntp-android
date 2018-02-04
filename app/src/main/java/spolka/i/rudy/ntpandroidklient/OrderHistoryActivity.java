package spolka.i.rudy.ntpandroidklient;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends Fragment {

    SessionManager currentSession;
    private ListView listView;
    private String jsonResult;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_order_history, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        currentSession = new SessionManager(getActivity());

        listView = getView().findViewById(R.id.listOrderHistory);

        final List<Map<String, String>> orderList = new ArrayList<Map<String, String>>();
        final String[] from = {"Data zlozenia zamowienia","Data realizacji zamowienia","Ilosc Produktu 1","Ilosc Produktu 2","Ilosc Produktu 3"};
        final int[] to = {R.id.orderListItem1,R.id.orderListItem2,R.id.orderListItem3,R.id.orderListItem4,R.id.orderListItem5};

        Response.Listener<String> responseListener = new Response.Listener<String>() {                              //oczekowanie na odpowiedz z bazy
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("emp_info");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        String DataZlozenia = jsonChildNode.getString("Data_Zlozenia");
                        String DataRealizacji;
                        if(jsonChildNode.getString("Data_Realizacji") == "null")
                            DataRealizacji = "Niezrealizowano";
                        else
                        DataRealizacji = jsonChildNode.getString("Data_Realizacji");
                        String IloscProduktu1 = jsonChildNode.getString("Ilosc_Produktu_1");
                        String IloscProduktu2 = jsonChildNode.getString("Ilosc_Produktu_2");
                        String IloscProduktu3= jsonChildNode.getString("Ilosc_Produktu_3");
                        String output = DataZlozenia + ":" + DataRealizacji + ":" + IloscProduktu1+ ":" + IloscProduktu2 + ":" + IloscProduktu3;
                        Log.e("TAG",output);
                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put("Data zlozenia zamowienia",DataZlozenia);
                        map.put("Data realizacji zamowienia",DataRealizacji);
                        map.put("Ilosc Produktu 1",IloscProduktu1);
                        map.put("Ilosc Produktu 2",IloscProduktu2);
                        map.put("Ilosc Produktu 3",IloscProduktu3);
                        orderList.add(map);

                        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), orderList, R.layout.order_list_layout,from ,to);
                        listView.setAdapter(simpleAdapter);
                        simpleAdapter.notifyDataSetChanged();
                        listView.invalidateViews();
                    }
                } catch (JSONException e) {
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("Data zlozenia zamowienia","Brak zamowien");
                    map.put("Data realizacji zamowienia"," ");
                    map.put("Ilosc Produktu 1"," ");
                    map.put("Ilosc Produktu 2"," ");
                    map.put("Ilosc Produktu 3"," ");
                    orderList.add(map);

                    SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), orderList, R.layout.order_list_layout,from ,to);
                    listView.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();
                    listView.invalidateViews();
                }


            }
        };




        HashMap<String,String> userCred = currentSession.getUserDetails();
        String userID = userCred.get(SessionManager.KEY_USER_ID);

        dbOrderHistoryRequest orderHistoryRequest = new dbOrderHistoryRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(orderHistoryRequest);

    }
}

