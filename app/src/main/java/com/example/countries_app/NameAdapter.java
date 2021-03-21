package com.example.countries_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.CountryNameViewHolder> {

    private String[] names;

    public NameAdapter(String[] names) { this.names = names;}


    @Override
    public CountryNameViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.country_name_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        CountryNameViewHolder viewHolder = new CountryNameViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CountryNameViewHolder holder, int position) {
        Log.v("pos", String.valueOf(position));
        holder.bind(names[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class CountryNameViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView listCountryNameView;

        public CountryNameViewHolder(View itemView) {
            super(itemView);

            listCountryNameView = (TextView) itemView.findViewById(R.id.tv_item_countryName);
        }


        void bind(String name) {
            listCountryNameView.setText(name);
        }
    }
}
