package com.example.countries_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.CountryNameViewHolder> implements Filterable {

    private final List<String> names;
    private final List<String> allNames;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(String clickedItemIndex);

    }

    public NameAdapter(List<String> names, ListItemClickListener listener) {
        this.names = new ArrayList<>(names);
        this.allNames = new ArrayList<>(names); //
        mOnClickListener = listener;
    }


    @NonNull
    @Override
    public CountryNameViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.country_name_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new CountryNameViewHolder(view);
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
            String clickedCountryName = String.valueOf(listCountryNameView.getText());
            mOnClickListener.onListItemClick(clickedCountryName);
        }
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allNames);
            } else {
                // regex string to match everything starting with the constraint
                String searchRegex = constraint.toString().toLowerCase().trim() + "(.*)";
                for (String name : allNames) {
                    if (name.toLowerCase().matches(searchRegex)) {
                        filteredList.add(name);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            names.clear();
            names.addAll((List<String>) results.values); // Unchecked cast but we know there are only strings in results so the warning can be suppressed
            notifyDataSetChanged();
        }
    };


}
