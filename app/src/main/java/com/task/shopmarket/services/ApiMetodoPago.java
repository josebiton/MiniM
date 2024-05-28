package com.task.shopmarket.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.task.shopmarket.models.MetodoPago;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiMetodoPago {

    public interface MetodoPagoCallback {
        void onSuccess(List<MetodoPago> metodoPagos);
        void onError(String errorMessage);
    }

    public static void obtenerMetodoPago(MetodoPagoCallback callback, Context context) {
        String url = BaseURL.API_URL + "metodo-pago-emp";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<MetodoPago> listaMetodoPago = parseJsonArray(response);
                        callback.onSuccess(listaMetodoPago);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error en la solicitud: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Code " + error.networkResponse.statusCode;
                        }
                        callback.onError(errorMessage + "\n" + error.toString());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public static List<MetodoPago> parseJsonArray(JSONArray jsonArray) {
        List<MetodoPago> listaMetodoPago = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int metodopago_id = jsonObject.getInt("metodo_pago_emp_id");
                String nombre = jsonObject.getString("metodo_pago_nombre");
                String logo = jsonObject.getString("metodo_pago_logo");
                MetodoPago metodoPago = new MetodoPago(metodopago_id, nombre, logo);
                listaMetodoPago.add(metodoPago);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listaMetodoPago;
    }
}
