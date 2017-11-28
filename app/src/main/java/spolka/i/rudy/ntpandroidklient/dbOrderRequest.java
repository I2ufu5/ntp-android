package spolka.i.rudy.ntpandroidklient;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rufus on 11/3/2017.
 */

public class dbOrderRequest extends StringRequest {

    private static final String ORDER_REQUEST_URL = "https://ntpprojekt.000webhostapp.com/ZamowOrder.php";
    private Map<String, String> params;

    public dbOrderRequest(int userID, String productCount1, String productCount2, String productCount3, Response.Listener<String> listener) {
        super(Method.POST, ORDER_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("userID",String.valueOf(userID));
        params.put("productCount1",productCount1);
        params.put("productCount2",productCount2);
        params.put("productCount3",productCount3);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }


}