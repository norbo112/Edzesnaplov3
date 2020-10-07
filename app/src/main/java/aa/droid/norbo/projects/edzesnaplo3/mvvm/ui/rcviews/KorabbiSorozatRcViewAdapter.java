package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmKorabbiSorozatRcitemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;

public class KorabbiSorozatRcViewAdapter extends RecyclerView.Adapter<KorabbiSorozatRcViewAdapter.KorabbiSorozatViewHolder> {
    private List<Sorozat> sorozats;
    private DateTimeFormatter dateTimeFormatter;

    public KorabbiSorozatRcViewAdapter(List<Sorozat> sorozats, DateTimeFormatter dateTimeFormatter) {
        this.sorozats = sorozats;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @NonNull
    @Override
    public KorabbiSorozatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MvvmKorabbiSorozatRcitemBinding binding = MvvmKorabbiSorozatRcitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new KorabbiSorozatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull KorabbiSorozatViewHolder holder, int position) {
        holder.bind(sorozats.get(position));
    }

    @Override
    public int getItemCount() {
        return sorozats.size();
    }

    static class KorabbiSorozatViewHolder extends RecyclerView.ViewHolder {
        MvvmKorabbiSorozatRcitemBinding itemBinding;

        public KorabbiSorozatViewHolder(MvvmKorabbiSorozatRcitemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        void bind(Sorozat sorozat) {
            itemBinding.korabbiSorozatInfo.setText(sorozat.toString());
        }
    }
}
