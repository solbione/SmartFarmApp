package smartfarm.team.smartfarmapp.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.HashMap;
import java.util.Map;

import smartfarm.team.smartfarmapp.HomePage.MainActivity;
import smartfarm.team.smartfarmapp.R;
import smartfarm.team.smartfarmapp.util.Constant;
import smartfarm.team.smartfarmapp.util.ServerRequest;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    String id;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.user_shared_preef),MODE_PRIVATE);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Bundle arg = intent.getExtras();
                if (arg != null && arg.getString("id") != null)
                    id = arg.getString("id");
                else
                    id = null;

                if(!Constant.CheckConnectivity(this)) {
                    this.startService(intent);
                    sharedPreferences.edit().putBoolean(getString(R.string.SENT_TOKEN_TO_SERVER), false).apply();
                    sharedPreferences.edit().putString(getString(R.string.token), token).apply();
                }
                else {
                    sendRegistrationToServer(token);

                    // You should store a boolean that indicates whether the generated token has been
                    // sent to your server. If the boolean is false, send the token to your server,
                    // otherwise your server should have already received the token.
                    sharedPreferences.edit().putBoolean(getString(R.string.SENT_TOKEN_TO_SERVER), true).apply();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);

            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(getString(R.string.SENT_TOKEN_TO_SERVER), false).apply();
        }
    }

    /**
     * Normally, you would want to persist the registration to third-party servers. Because we do
     * not have a server, and are faking it with a website, you'll want to smartfarm.team.smartfarmapp.log the token instead.
     * That way you can see the value in logcat, and note it for future use in the website.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        String url = Constant.url+"/sf/postid";
        StringRequest tokenRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                SharedPreferences details = MainActivity.thisAct.
                        getSharedPreferences(getString(R.string.shared_main_name), Context.MODE_PRIVATE);
                param.put("UserID",details.getString(getString(R.string.shared_farm_id),"001"));
                param.put("IID", token);
                return param;
            }
        };
        ServerRequest.getInstance(this).getRequestQueue().add(tokenRequest);
    }
}