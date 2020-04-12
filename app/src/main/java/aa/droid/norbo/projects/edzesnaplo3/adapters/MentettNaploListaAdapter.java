package aa.droid.norbo.projects.edzesnaplo3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.DateTimeFormatter;

public class MentettNaploListaAdapter extends BaseAdapter {
    private Context context;
    private List<Naplo> naplos;

    public MentettNaploListaAdapter(Context context, List<Naplo> naplos) {
        this.context = context;
        this.naplos = naplos;
    }

    public void addNaplo(Naplo naplo) {
        naplos.add(naplo);
        notifyDataSetChanged();
    }

    public void addAll(List<Naplo> mNaplok) {
        naplos.addAll(mNaplok);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return naplos.size();
    }

    @Override
    public Object getItem(int position) {
        return naplos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.mentett_naplo_item_0, parent, false);
        }

        final Naplo selected = naplos.get(position);

        ((TextView)convertView.findViewById(R.id.tvNaploListaItem)).setText(
                DateTimeFormatter.getNaploListaDatum(selected.getNaplodatum()));

        return convertView;
    }

    public void clear() {
        naplos.clear();
        notifyDataSetChanged();
    }
}
