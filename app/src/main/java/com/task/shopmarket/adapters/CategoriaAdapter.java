package com.task.shopmarket.adapters;

import static com.task.shopmarket.services.BaseURL.URL_IMG;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.task.shopmarket.R;
import com.task.shopmarket.models.Categoria;
import com.task.shopmarket.views.ProductoCategoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> {
    List<Categoria> listaCategoria;
    Context context;
    String Tag;

    public CategoriaAdapter(List<Categoria> listaCategoria, Context context, String tag) {
        this.listaCategoria = listaCategoria;
        this.context = context;
        this.Tag = tag;
    }

    public CategoriaAdapter(ArrayList<Categoria> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
        if (Tag.equalsIgnoreCase("Inicio")) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria_inicio, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Categoria categoria = listaCategoria.get(position);
        holder.nombre.setText(categoria.getNombre());

        if (Tag.equalsIgnoreCase("Categoria")) {
            Picasso.get()
                    .load(URL_IMG + categoria.getImagen())
                    .into(holder.imagen, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            Picasso.get()
                    .load(categoria.getImagen())
                    .into(holder.imagen);
        }

        holder.ll_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categoriaId = categoria.getId();
                Intent intent = new Intent(context, ProductoCategoria.class);
                intent.putExtra("categoria_id", categoriaId);
                intent.putExtra("categoria_nombre", categoria.getNombre());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (Tag.equalsIgnoreCase("Inicio") && listaCategoria.size() < 6 && listaCategoria.size() > 3) {
            return 3;
        } else if (Tag.equalsIgnoreCase("Inicio") && listaCategoria.size() >= 6) {
            return 6;
        } else {
            return listaCategoria.size();
        }
    }

    public void setCategorias(List<Categoria> listaCategorias) {
        this.listaCategoria = listaCategorias;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre;
        ProgressBar progressBar;
        CardView cardView;
        LinearLayout ll_categoria;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.categoria_imagen);
            nombre = itemView.findViewById(R.id.categoria_nombre);
            progressBar = itemView.findViewById(R.id.progressbar);
            cardView = itemView.findViewById(R.id.card_view);
            ll_categoria = itemView.findViewById(R.id.ll_categoria);
        }
    }
}
