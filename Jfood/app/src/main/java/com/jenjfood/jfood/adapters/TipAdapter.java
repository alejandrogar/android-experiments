package com.jenjfood.jfood.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jenjfood.jfood.R;
import com.jenjfood.jfood.objects.Tip;

import java.util.ArrayList;
import java.util.List;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder>{

    List<Tip> items = new ArrayList<>();

    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView body;
        public CardView card;

        private TipAdapter padre = null;

        public ViewHolder(View v, TipAdapter padre) {
            super(v);

            this.padre = padre;
            
            body = (TextView) v.findViewById(R.id.body);
            card = (CardView) v.findViewById(R.id.card);

        }
    }

    public TipAdapter(List<Tip> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = null;
        int layoutId = -1;
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_tip, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.body.setText(items.get(i).getBody());

        final int item = i;

        viewHolder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }
}