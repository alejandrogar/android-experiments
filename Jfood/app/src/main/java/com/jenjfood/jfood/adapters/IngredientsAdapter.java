package com.jenjfood.jfood.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jenjfood.jfood.R;
import com.jenjfood.jfood.objects.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16/04/16.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{

    List<Ingredient> items = new ArrayList<>();

    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView name;
        public CheckBox checkIngredient;

        private IngredientsAdapter padre = null;

        public ViewHolder(View v, IngredientsAdapter padre) {
            super(v);

            this.padre = padre;


            name = (TextView) v.findViewById(R.id.name_ingredient);
            checkIngredient = (CheckBox) v.findViewById(R.id.checkIngredient);

        }
    }

    public IngredientsAdapter(List<Ingredient> items, Context context) {
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
                .inflate(R.layout.item_ingredient, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.name.setText(items.get(i).getName());

        final int item = i;

        viewHolder.checkIngredient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    viewHolder.name.setPaintFlags( viewHolder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    viewHolder.name.setPaintFlags(viewHolder.name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

    }
    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }
}
