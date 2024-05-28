package com.task.shopmarket.adapters;

import static com.task.shopmarket.services.BaseURL.URL_IMG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.task.shopmarket.R;
import com.task.shopmarket.models.MetodoPago;

import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.MyViewHolder> {

    List<MetodoPago> listaPago;
    Context context;
    private OnItemClickListener itemClickListener;
    private int selectedItem = -1;

    public PagoAdapter(List<MetodoPago> pago, Context context) {
        this.listaPago = pago;
        this.context = context;
    }

    public void setPagoList(List<MetodoPago> pago) {
        this.listaPago = pago;
    }

    public interface OnItemClickListener {
        void onItemClick(MetodoPago pago);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public PagoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_metodo_pago, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MetodoPago metodoPago = listaPago.get(position);

        holder.pago_nombre.setText(metodoPago.getNombre());
        Picasso.get().load(URL_IMG + metodoPago.getLogo()).into(holder.pago_logo, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.pg_progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                holder.pg_progressbar.setVisibility(View.GONE);
            }
        });

        holder.pago_item.setBackgroundColor(selectedItem == position ? ContextCompat.getColor(context, R.color.selec) : Color.TRANSPARENT);
        holder.pago_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    selectedItem = position;
                    notifyDataSetChanged();
                    itemClickListener.onItemClick(metodoPago);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaPago !=null ? listaPago.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pago_nombre ;
        ImageView pago_logo;
        LinearLayout pago_item;
        ProgressBar pg_progressbar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pago_nombre = itemView.findViewById(R.id.pg_nombre);
            pago_logo = itemView.findViewById(R.id.pg_logo);
            pago_item = itemView.findViewById(R.id.pago_item);
            pg_progressbar = itemView.findViewById(R.id.pg_progressbar);

        }
    }
}
