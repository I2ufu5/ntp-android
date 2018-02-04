package spolka.i.rudy.ntpandroidklient;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rufus on 11/28/2017.
 */

public class dbOrderHistoryRequest extends StringRequest{

    private static final String ORDERHISTORY_REQUEST_URL = "https://ntpprojekt.000webhostapp.com/historiazamowien.php";
    private Map<String, String> params;

    public dbOrderHistoryRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, ORDERHISTORY_REQUEST_URL, listener, null);
        params = new HashMap<>();
        Log.e("TAG",userID);
        params.put("ID_uzytkownika", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
