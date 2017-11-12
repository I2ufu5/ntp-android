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

    private static final String REGISTER_REQUEST_URL = "https://ntpprojekt.000webhostapp.com/ZamowOrder.php";                       // pehap nie opisany !!!!!!!!
    private Map<String, String> params;

    public dbOrderRequest(int userID, int productCount1,int productCount2, int productCount3, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("userID",String.valueOf(userID));
        params.put("prooductCount1",String.valueOf(productCount1));
        params.put("prooductCount2",String.valueOf(productCount2));
        params.put("prooductCount3",String.valueOf(productCount3));
        Log.e("Tag", String.valueOf(productCount1));
        Log.e("Tag", String.valueOf(productCount2));
        Log.e("Tag", String.valueOf(productCount3));
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }


}