package aa.droid.norbo.projects.edzesnaplo3.rcview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag.Elelmiszer;

public class TapanyagAdapter extends ArrayAdapter<Elelmiszer>
    implements Filterable {


    private static class ViewHolder {
        TextView tapnev, tapfajta, tapkj, tapkcal, tapszen, tapfeh, tapzsir, taprost;
    }

    private Context context;
    private List<Elelmiszer> elelmiszers;
    private List<Elelmiszer> elelmiszersOrig;
    private NevFajtaFilter filter;
    private int filter_is_tipus = 0;

    public TapanyagAdapter(Context context, List<Elelmiszer> elelmiszers) {
        super(context, R.layout.tapanyag_row, elelmiszers);
        this.context = context;
        this.elelmiszers = elelmiszers;
        this.elelmiszersOrig = elelmiszers;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Elelmiszer elelmiszer = elelmiszers.get(position);

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tapanyag_row, parent, false);
            viewHolder.tapnev = convertView.findViewById(R.id.tap_nev);
            viewHolder.tapfajta = convertView.findViewById(R.id.tap_fajta);
            viewHolder.tapkj = convertView.findViewById(R.id.tap_kj);
            viewHolder.tapkcal = convertView.findViewById(R.id.tap_kcal);
            viewHolder.tapszen = convertView.findViewById(R.id.tap_szen);
            viewHolder.tapfeh = convertView.findViewById(R.id.tap_feh);
            viewHolder.tapzsir = convertView.findViewById(R.id.tap_zsir);
            viewHolder.taprost = convertView.findViewById(R.id.tap_rost);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tapnev.setText(elelmiszer.getNev());
        viewHolder.tapfajta.setText(elelmiszer.getFajta());
        viewHolder.tapkj.setText(elelmiszer.getKj()+" kj");
        viewHolder.tapkcal.setText(elelmiszer.getKcal()+" kcal");
        viewHolder.tapszen.setText(elelmiszer.getSzenhidrat()+" g");
        viewHolder.tapfeh.setText(elelmiszer.getFeherje()+" g");
        viewHolder.tapzsir.setText(elelmiszer.getZsir()+" g");
        viewHolder.taprost.setText(elelmiszer.getRost()+" g");

        return convertView;
    }

    @Override
    public int getCount() {
        return elelmiszers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public Elelmiszer getItem(int position) {
        return elelmiszers.get(position);
    }

    public void setElelmiszers(List<Elelmiszer> elelmiszers) {
        this.elelmiszers = elelmiszers;
        this.elelmiszersOrig = elelmiszers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(filter == null)
            filter = new NevFajtaFilter();
        return filter;
    }

    public void setFilter_is_tipus(int filter_is_tipus) {
        this.filter_is_tipus = filter_is_tipus;
    }

    public int getFilter_is_tipus() {
        return filter_is_tipus;
    }

    private class NevFajtaFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                List<Elelmiszer> filterList = new ArrayList<>();
                for(Elelmiszer elelem: elelmiszersOrig) {
                    if(filter_is_tipus != 0) {
                        if (elelem.getFajta().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filterList.add(elelem);
                        }
                    } else {
                        if (elelem.getNev().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                elelem.getFajta().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filterList.add(elelem);
                        }
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = elelmiszersOrig.size();
                results.values = elelmiszersOrig;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                elelmiszers = (List<Elelmiszer>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
