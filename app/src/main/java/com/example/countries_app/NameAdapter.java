package com.example.countries_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.CountryNameViewHolder> {

    private List<String> names;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(String clickedItemIndex);

    }

    public NameAdapter(List<String> names, ListItemClickListener listener) {
        this.names = names;
        mOnClickListener = listener;
    }


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
        holder.bind(names.get(position));
    }

    @Override
    public int getItemCount() {

        return names.size();
    }

    class CountryNameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView listCountryNameView;

        public CountryNameViewHolder(View itemView) {
            super(itemView);

            listCountryNameView = (TextView) itemView.findViewById(R.id.tv_item_countryName);
            itemView.setOnClickListener(this);
        }


        void bind(String name) {
            listCountryNameView.setText(name);
        }

        @Override
        public void onClick(View view) {
            // pos not needed?
            int clickedPos = getAdapterPosition();
            String clickedCountryName = String.valueOf(listCountryNameView.getText());

            Log.v("tw", String.valueOf(listCountryNameView.getText()));

            mOnClickListener.onListItemClick(clickedCountryName);
        }
    }


}
