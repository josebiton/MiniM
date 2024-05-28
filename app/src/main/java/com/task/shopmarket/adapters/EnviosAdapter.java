package com.task.shopmarket.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.task.shopmarket.R;
import com.task.shopmarket.models.MetodoEnvio;


import java.util.List;
import java.util.Locale;

public class EnviosAdapter extends RecyclerView.Adapter<EnviosAdapter.MyViewHolder> {

    List<MetodoEnvio> lisaMetodoEnvios;
    Context context;
    private OnItemClickListener itemClickListener;
    private int selectedItem = -1;

    public EnviosAdapter(List<MetodoEnvio> metodoEnvios, Context context) {
        this.lisaMetodoEnvios = metodoEnvios;
        this.context = context;
    }
    public void setMetodoEnvioList(List<MetodoEnvio> metodoEnvios) {
        this.lisaMetodoEnvios = metodoEnvios;
    }
    public interface OnItemClickListener {
        void onItemClick(MetodoEnvio metodoEnvio);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }


    @NonNull
    @Override
    public EnviosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_metodo_envio, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnviosAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MetodoEnvio metodoEnvio = lisaMetodoEnvios.get(position);

        holder.env_nombre.setText(metodoEnvio.getNombre());
        holder.env_descripcion.setText(metodoEnvio.getDescripcion());
        String precio = NumberFormat.getCurrencyInstance(new Locale("es", "PE")).format(metodoEnvio.getPrecio());
        holder.env_precio.setText(precio);

        holder.envio_item.setBackgroundColor(selectedItem == position ? ContextCompat.getColor(context, R.color.selec) : Color.TRANSPARENT);
        holder.envio_item.setOnClickListener(v -> {
            if (itemClickListener != null) {
                selectedItem = position;
                notifyDataSetChanged();

                itemClickListener.onItemClick(metodoEnvio);
            }
        });


    }

    @Override
    public int getItemCount() {
        return lisaMetodoEnvios != null ?  lisaMetodoEnvios.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout envio_item;
        TextView env_nombre, env_descripcion, env_precio;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            envio_item = itemView.findViewById(R.id.envio_item);
            env_nombre = itemView.findViewById(R.id.env_nombre);
            env_descripcion = itemView.findViewById(R.id.env_descripcion);
            env_precio = itemView.findViewById(R.id.env_precio);


        }
    }

}
