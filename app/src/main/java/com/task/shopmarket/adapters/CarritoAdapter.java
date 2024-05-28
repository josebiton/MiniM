package com.task.shopmarket.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.task.shopmarket.R;
import com.task.shopmarket.models.Carrito;
import com.task.shopmarket.utils.EscuchaCarrito;
import com.task.shopmarket.utils.LocalStorage;
import com.task.shopmarket.views.CarritoActivity;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {
    private List<Carrito> listaCarrito;
    private Context context;

    private EscuchaCarrito escuchaCarrito;

    public CarritoAdapter(List<Carrito> listaCarrito, Context context) {
        this.listaCarrito = listaCarrito;
        this.context = context;
    }

    @NonNull
    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoAdapter.ViewHolder holder, int position) {

        Carrito carritoItem = listaCarrito.get(position);

        holder.txt_nombre_producto.setText(carritoItem.getNombre());
        String precioConMoneda = "S/ " + carritoItem.getPrecio();
        holder.txt_precio.setText(precioConMoneda);
        holder.txt_cantidad.setText(String.valueOf(carritoItem.getCantidad()));

        String primeraImagen = carritoItem.getImagen().split(",")[0];
        Picasso.get().load(primeraImagen).error(R.drawable.no_imagen).into(holder.img_carrito, new Callback() {
            @Override
            public void onSuccess() {
                if (holder.progressbar != null) {
                    holder.progressbar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.d("Error : ", e.getMessage());
            }
        });


        // TODO: Implementar el método onClick para el botón de Sumar cantidad
        holder.cart_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad = carritoItem.getCantidad();
                cantidad++;

                carritoItem.setCantidad(cantidad);
                LocalStorage.guardarCarrito(context, listaCarrito);

                notifyDataSetChanged();

                if (context instanceof CarritoActivity) {
                    ((CarritoActivity) context).actualizarTotalPrecio();
                }
            }
        });

        // TODO: Implementar el método onClick para el botón de Restar cantidad
        holder.cart_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad = carritoItem.getCantidad();
                cantidad--;

                if (cantidad == 0) {
                    listaCarrito.remove(carritoItem);
                    LocalStorage.guardarCarrito(context, listaCarrito);

                    if (escuchaCarrito != null) {
                        escuchaCarrito.onCarritoChange(listaCarrito);
                    }
                } else {
                    carritoItem.setCantidad(cantidad);
                    LocalStorage.guardarCarrito(context, listaCarrito);
                }

                notifyDataSetChanged();

                if (context instanceof CarritoActivity) {
                    ((CarritoActivity) context).actualizarTotalPrecio();
                }
            }
        });

        // TODO: Implementar el método onClick para el botón de eliminar
        holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalStorage.eliminarProductoDelCarrito(context, carritoItem);
                listaCarrito.remove(carritoItem);
                notifyDataSetChanged();

                if (escuchaCarrito != null) {
                    escuchaCarrito.onCarritoChange(listaCarrito);
                }

                if (context instanceof CarritoActivity) {
                    ((CarritoActivity) context).actualizarTotalPrecio();
                }
            }
        });
    }

    public void actualizarCarrito(List<Carrito> nuevoCarrito) {
        this.listaCarrito = nuevoCarrito;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaCarrito != null ? listaCarrito.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_carrito;
        TextView txt_nombre_producto, txt_cantidad;
        TextView txt_precio;
        Button btn_eliminar, cart_mas, cart_menos;
        ProgressBar progressbar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            progressbar = itemView.findViewById(R.id.progressbar);
            img_carrito = itemView.findViewById(R.id.img_carrito);
            txt_nombre_producto = itemView.findViewById(R.id.txt_nombre_producto);
            txt_cantidad = itemView.findViewById(R.id.txt_cantidad);
            txt_precio = itemView.findViewById(R.id.txt_precio);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
            cart_mas = itemView.findViewById(R.id.cart_mas);
            cart_menos = itemView.findViewById(R.id.cart_menos);
        }
    }
}
