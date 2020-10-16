package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.TevekenysegEdzestervValasztoDialogItemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class EdzesTervValasztoDialog {
    private EdzesTervViewModel edzesTervViewModel;
    private EdzesTervKeszito edzesTervKeszito;

    public interface TervValasztoInterface {
        void tervValasztva(EdzesTerv edzesTerv);
    }

    @Inject
    public EdzesTervValasztoDialog(EdzesTervViewModel edzesTervViewModel, EdzesTervKeszito edzesTervKeszito) {
        this.edzesTervViewModel = edzesTervViewModel;
        this.edzesTervKeszito = edzesTervKeszito;
    }

    public void makeValasztoDialog(LifecycleOwner owner, Context context, TervValasztoInterface tervValasztoInterface) {
        edzesTervViewModel.getEdzestervek().observe(owner, edzesTervWithEdzesnaps -> {
            if(edzesTervWithEdzesnaps != null && edzesTervWithEdzesnaps.size() > 0) {
                List<EdzesTerv> edzesTervs = edzesTervKeszito.makeEdzesterv(edzesTervWithEdzesnaps);
                ArrayAdapter<EdzesTerv> adapter = getTervListAdapter(context, edzesTervs);


                new AlertDialog.Builder(context)
                        .setTitle("Edzésterv kiválasztása")
                        .setAdapter(adapter, (dialog, which) -> {
                            tervValasztoInterface.tervValasztva(adapter.getItem(which));
                        })
                        .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Edzésterv kiválasztása")
                        .setMessage("Nincs megjeleníthető edzésterv")
                        .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    public ArrayAdapter<EdzesTerv> getTervListAdapter(Context context, List<EdzesTerv> edzesTervs) {
        return new ArrayAdapter<EdzesTerv>(context, R.layout.tevekenyseg_edzesterv_valaszto_dialog_item, edzesTervs) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                @SuppressLint("ViewHolder") TevekenysegEdzestervValasztoDialogItemBinding binding = TevekenysegEdzestervValasztoDialogItemBinding.inflate(
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), parent, false);

                EdzesTerv item = getItem(position);
                binding.tevekenysegTervValasztoMegnevezes.setText(item.getMegnevezes());
                binding.tevekenysegTervValasztoReszletek.setText(getReszletek(item));
                return binding.getRoot();
            }

            private String getReszletek(EdzesTerv edzesTerv) {
                StringBuilder sb = new StringBuilder();
                for (Edzesnap edzesnap: edzesTerv.getEdzesnapList()) {
                    sb.append(edzesnap.getEdzesNapLabel()).append(" ");
                    String csoportok = edzesnap.getValasztottCsoport().stream().map(Csoport::getIzomcsoport).collect(Collectors.joining(","));
                    if(!edzesnap.getEdzesNapLabel().contains("pihenőnap"))
                        sb.append(": ").append(csoportok).append(" ");

                    sb.append("\n");
                }
                return sb.toString();
            }
        };
    };
}
