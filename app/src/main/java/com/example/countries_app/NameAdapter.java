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

/**
 * Adapter for RecyclerView
 */
public class NameAdapter extends RecyclerView.Adapter<NameAdapter.CountryNameViewHolder> implements Filterable {

    private final List<String> names;
    private final List<String> allNames;

    final private ListItemClickListener mOnClickListener;

    // Inteface recieves onClicks
    public interface ListItemClickListener {
        void onListItemClick(String clickedItemIndex);
    }

    // Adapter that recieves names and adds above listener to the name
    public NameAdapter(List<String> names, ListItemClickListener listener) {
        this.names = new ArrayList<>(names);
        this.allNames = new ArrayList<>(names); //
        mOnClickListener = listener;
    }


    /**
     * @return viewHolder for each countryname
     */
    @NonNull
    @Override
    public CountryNameViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.country_name_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new CountryNameViewHolder(view);
    }

    /**
     * update the contents of the ViewHolder to display the name,
     * with help of the provided position
     */
    @Override
    public void onBindViewHolder(CountryNameViewHolder holder, int position) {
        holder.bind(names.get(position));
    }

    /**
     * @return size of displayed items
     */
    @Override
    public int getItemCount() {
        return names.size();
    }

    class CountryNameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView listCountryNameView;

        public CountryNameViewHolder(View itemView) {
            super(itemView);
            listCountryNameView = (TextView) itemView.findViewById(R.id.tv_item_countryName);
            itemView.setOnClickListener(this);
        }

        /**
         * helper method to bind the name to the view with onBindViewHolder
         */
        void bind(String name) {
            listCountryNameView.setText(name);
        }

        @Override
        public void onClick(View view) {
            String clickedCountryName = String.valueOf(listCountryNameView.getText());
            mOnClickListener.onListItemClick(clickedCountryName);
        }
    }

    /**
     * @return Filter that's used by nameAdapter to display names based on user input
     */
    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) { // If searchfield is empty, we display the all names
                filteredList.addAll(allNames);
            } else {                                              // else we use regex to filter all names based on input (constraint param)
                // regex string to match everything starting with the constraint
                String searchRegex = constraint.toString().toLowerCase().trim() + "(.*)";
                for (String name : allNames) {
                    if (name.toLowerCase().matches(searchRegex)) { // compare to each name in allNames and add to filteredList if matched
                        filteredList.add(name);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        /**
         *  After perfomFiltering returns, we publish results by
         *  clearing existing names and add the filtered ones
         *  to update recyclerView we call notifyDataSetChanged();
         */
        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            names.clear();
            names.addAll((List<String>) results.values); // Unchecked cast but we know there are only strings in results so the warning can be suppressed
            notifyDataSetChanged();
        }
    };


}
