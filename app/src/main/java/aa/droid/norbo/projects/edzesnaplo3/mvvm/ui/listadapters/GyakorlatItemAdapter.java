package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.listadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatListItemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;

public class GyakorlatItemAdapter extends BaseAdapter implements Filterable {
    private List<GyakorlatUI> gyakorlats;
    private List<GyakorlatUI> gyakorlatsFull;
    private CsoportFilter csoportFilter;
    private Context context;
    private MvvmGyakorlatListItemBinding itemBinding;

    public GyakorlatItemAdapter(List<GyakorlatUI> gyakorlats, Context context) {
        this.gyakorlats = gyakorlats;
        this.gyakorlatsFull = gyakorlats;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gyakorlats.size();
    }

    @Override
    public Object getItem(int position) {
        return gyakorlats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        if(csoportFilter == null)
            csoportFilter = new CsoportFilter();
        return csoportFilter;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mvvm_gyakorlat_list_item, parent, false);
            itemBinding = DataBindingUtil.bind(convertView);
            convertView.setTag(itemBinding);
        } else {
            itemBinding = (MvvmGyakorlatListItemBinding) convertView.getTag();
        }

        itemBinding.setGyakorlat(gyakorlats.get(position));

        return itemBinding.getRoot();
    }

    class CsoportFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                List<GyakorlatUI> filterList = new ArrayList<>();
                for(GyakorlatUI rssItem: gyakorlatsFull) {
                    if(rssItem.getCsoport().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            rssItem.getMegnevezes().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(rssItem);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = gyakorlatsFull.size();
                results.values = gyakorlatsFull;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                gyakorlats = (List<GyakorlatUI>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}