package aa.droid.norbo.projects.edzesnaplo3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.DateTimeFormatter;

public class MentettNaploListaAdapter extends BaseAdapter {
    private Context context;
    private List<NaploWithSorozat> naplos;

    public MentettNaploListaAdapter(Context context, List<NaploWithSorozat> naplos) {
        this.context = context;
        this.naplos = naplos;
    }

    public void addNaplo(NaploWithSorozat naplo) {
        naplos.add(naplo);
        notifyDataSetChanged();
    }

    public void addAll(List<NaploWithSorozat> mNaplok) {
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

        final NaploWithSorozat selected = naplos.get(position);

        ((TextView)convertView.findViewById(R.id.tvNaploListaItem)).setText(DateTimeFormatter.getNaploListaDatum(selected.daonaplo.getNaplodatum()));
        ((TextView)convertView.findViewById(R.id.tvMentettGyakorlatok)).setText(appendGyakorlatStr(selected.sorozats));

        if(selected.daonaplo.getCommentFilePath() != null && selected.daonaplo.getCommentFilePath().length() > 0) {
            convertView.findViewById(R.id.ivCommentOn).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.ivCommentOn).setVisibility(View.GONE);
        }

        return convertView;
    }

    private String appendGyakorlatStr(List<SorozatWithGyakorlat> sorozats) {
        StringBuilder builder = new StringBuilder(" ");
        Set<String> stringset = new HashSet<>();
        for (SorozatWithGyakorlat s: sorozats) {
            stringset.add(s.gyakorlat.getCsoport());
        }

        Iterator<String> iterator = stringset.iterator();
        while(iterator.hasNext()) {
            builder.append(" - ").append(iterator.next()).append("\n");
        }

        return builder.toString();
    }

    public void clear() {
        naplos.clear();
        notifyDataSetChanged();
    }
}
