package com.task.shopmarket.views.fragments;

import static com.task.shopmarket.services.BaseURL.API_URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.task.shopmarket.R;
import com.task.shopmarket.adapters.PagoAdapter;
import com.task.shopmarket.models.Carrito;
import com.task.shopmarket.models.MetodoPago;
import com.task.shopmarket.services.ApiMetodoPago;
import com.task.shopmarket.utils.LocalStorage;
import com.task.shopmarket.views.MainActivity;
import com.task.shopmarket.views.modal.YapeQrFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MetodoPagoFragment extends Fragment {
    List<Carrito> listaCarrito;
    View view;
    RecyclerView rv_metodo_pago;
    PagoAdapter pagoAdapter;
    Button btn_siguiente_confirmar, cargarEvidencia;
    private MetodoPago metodoPagoSeleccionado;
    LocalStorage localStorage;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    private boolean procesoCompletado = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_metodo_pago, container, false);

        progressDialog = new ProgressDialog(getContext());
        localStorage = new LocalStorage(getContext());
        listaCarrito = LocalStorage.obtenerCarrito(requireContext());
        rv_metodo_pago = view.findViewById(R.id.rv_pago);
        rv_metodo_pago.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pagoAdapter = new PagoAdapter(new ArrayList<>(), getContext());
        pagoAdapter.setOnItemClickListener(new PagoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MetodoPago metodoPago) {

                if (Objects.equals(metodoPago.getNombre(), "Yape")) {
                    abrirYapeQR();

                }
                metodoPagoSeleccionado = metodoPago;
                int idMetodoPago = metodoPagoSeleccionado.getId();
                localStorage = new LocalStorage(getContext());
                localStorage.capturarMetodoPago(idMetodoPago);
                Toast.makeText(getContext(), "Elemento seleccionado: " + metodoPago.getNombre() + metodoPago.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        rv_metodo_pago.setAdapter(pagoAdapter);
        obtenerMetodoPago();

        cargarEvidencia = view.findViewById(R.id.cargarEvidencia);
        cargarEvidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "51916834179";
                String message = "Aquí está la evidencia del pago.";
                try {
                    message = URLEncoder.encode(message, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String whatsappLink = "https://wa.me/" + phoneNumber + "?text=" + message;

                gotoUrl(whatsappLink);
            }
        });


        Button btnAbrirWhatsApp = view.findViewById(R.id.btnAbrirWhatsApp);
        btnAbrirWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = "com.whatsapp";
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);

                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_siguiente_confirmar = view.findViewById(R.id.btn_siguiente_confirmar);
        btn_siguiente_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (metodoPagoSeleccionado != null) {
                    if (procesoCompletado) {
                        confirmarPedido();
                    } else {
                        Toast.makeText(getContext(), "Complete el proceso antes de confirmar el pedido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Seleccione un método de pago", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
        procesoCompletado = true;
        btn_siguiente_confirmar.setEnabled(true);

    }
    private void confirmarPedido() {
        progressDialog.setMessage("Procesando compra...");
        progressDialog.show();
        // Obtener los datos del usuario del almacenamiento local
        sharedPreferences = requireContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String idUsuario = sharedPreferences.getString("id_usuario", "");
        String idPersona = sharedPreferences.getString("persona_id_usuario", "");
        String idDireccion = sharedPreferences.getString("direccionEnvio", "");
        String idMetodoPago = sharedPreferences.getString("metodoPago", "");
        String idMetodoEnvio = sharedPreferences.getString("metodoEnvio", "");
        String precioEnvio = sharedPreferences.getString("precioEnvio", "");
        String totalPagar = sharedPreferences.getString("totalCompra", "");
        String sumatotal = String.valueOf(Double.parseDouble(totalPagar) + Double.parseDouble(precioEnvio));
        String fechaPedido = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String fechaEntrega = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String extras = "Alguna información extra";
        String comentarios = "Algunos comentarios";

        // Crear el objeto JSON para los datos del pedido
        JSONObject datosPedido = new JSONObject();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try {
            datosPedido.put("persona_id", idPersona);
            datosPedido.put("users_id", idUsuario);
            datosPedido.put("metodo_pago_emp_id", idMetodoPago);
            datosPedido.put("metodo_envio_emp_id", idMetodoEnvio);
            datosPedido.put("direciones_id", idDireccion);
            datosPedido.put("fecha_pedido", fechaPedido);
            datosPedido.put("fecha_entrega", fechaEntrega);
            datosPedido.put("extras", extras);
            datosPedido.put("comentarios", comentarios);
            datosPedido.put("total_pagar", decimalFormat.format(Double.parseDouble(sumatotal)));
            datosPedido.put("precio_envio", precioEnvio);
        } catch (JSONException e) {
            e.printStackTrace();}

        JSONArray detallesPedido = new JSONArray();
        for (Carrito item : listaCarrito) {
            JSONObject detalleProducto = new JSONObject();
            try {
                detalleProducto.put("nombre_producto", item.getNombre());
                detalleProducto.put("producto_detalle_id", item.getId());
                detalleProducto.put("cantidad", item.getCantidad());
                double precio = Double.parseDouble(item.getPrecio().replace("S/. ", ""));
                detalleProducto.put("precio", decimalFormat.format(precio));
                double totalDetalle = item.getCantidad() * Double.parseDouble(item.getPrecio().replace("S/. ", ""));
                detalleProducto.put("total", decimalFormat.format(totalDetalle));
                detallesPedido.put(detalleProducto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("datos_pedido", datosPedido);
            jsonObject.put("detalles_pedido", detallesPedido);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_URL + "/pedidos", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
                LocalStorage.eliminarCarrito(requireContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ConfirmarFragment()).commit();
                    }
                }, 1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), "Error al realizar el pedido", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);
    }
    private void abrirYapeQR() {
        YapeQrFragment yapeQrDialogFragment = new YapeQrFragment();
        yapeQrDialogFragment.show(getFragmentManager(), "YapeQrDialog");
    }
    private void obtenerMetodoPago() {
        Context context = getContext();
        ApiMetodoPago.obtenerMetodoPago(new ApiMetodoPago.MetodoPagoCallback() {
            @Override
            public void onSuccess(List<MetodoPago> metodoPagos) {
                pagoAdapter.setPagoList(metodoPagos);
                pagoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }, context);
    }
}