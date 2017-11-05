package spolka.i.rudy.ntpandroidklient;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rufus on 11/3/2017.
 */

public class dbRegisterRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "https://ntpprojekt.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public dbRegisterRequest(String name, String secondname,String login, String password, String email, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("name",name);
        params.put("secondname",secondname);
        params.put("login",login);
        params.put("password",password);
        params.put("email",email);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }


}
