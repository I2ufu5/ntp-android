package spolka.i.rudy.ntpandroidklient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import java.util.HashMap;


/**
 * Created by Rufus on 11/9/2017.
 */

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "NtpSession";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USER_ID = "userID";


    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession(String userID, String name, String email){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID, userID);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID,null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }
}