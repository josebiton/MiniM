package com.task.shopmarket.services;



import static com.task.shopmarket.services.BaseURL.API_URL;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ServiceAuth {

    private static RequestQueue requestQueue;
    private Context context;

    public ServiceAuth(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    // Funcion para iniciar sesion
    public static void login(String email, String password, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = API_URL + "auth/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // Funcion para registrar usuario
    public static void register(String name, String email, String phone, String password, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = API_URL + "auth/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
