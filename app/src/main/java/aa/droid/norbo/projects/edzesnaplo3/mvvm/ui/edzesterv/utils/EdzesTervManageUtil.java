package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmDialogLoadEdzestervLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzestervTitleDialogLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.TevekenysegEdzestervValasztoDialogItemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.edzesterv.EdzesTervViewModel;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

/**
 * Edzésterv megjelenítése - lista vagy egy terv - dialogban, dialogból való terv választás
 */
@ActivityScoped
public class EdzesTervManageUtil {
    private EdzesTervViewModel edzesTervViewModel;
    private EdzesTervKeszito edzesTervKeszito;
    private Context context;

    public interface TervValasztoInterface {
        void tervValasztva(EdzesTerv edzesTerv);
    }

    @Inject
    public EdzesTervManageUtil(@ActivityContext Context context, EdzesTervViewModel edzesTervViewModel, EdzesTervKeszito edzesTervKeszito) {
        this.context = context;
        this.edzesTervViewModel = edzesTervViewModel;
        this.edzesTervKeszito = edzesTervKeszito;
    }

    public void makeValasztoDialog(LifecycleOwner owner, Context context, TervValasztoInterface tervValasztoInterface) {
        edzesTervViewModel.getEdzestervek().observe(owner, edzesTervWithEdzesnaps -> {
            if (edzesTervWithEdzesnaps != null && edzesTervWithEdzesnaps.size() > 0) {
                List<EdzesTerv> edzesTervs = edzesTervKeszito.makeEdzesterv(edzesTervWithEdzesnaps);
                ArrayAdapter<EdzesTerv> adapter = getTervListAdapter(context, edzesTervs);


                new AlertDialog.Builder(context)
                        .setTitle("Edzésterv kiválasztása")
                        .setAdapter(adapter, (dialog, which) -> tervValasztoInterface.tervValasztva(adapter.getItem(which)))
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

    public void viewEdzesTervDialog(EdzesTerv edzesTerv) {
        MvvmDialogLoadEdzestervLayoutBinding binding = MvvmDialogLoadEdzestervLayoutBinding.inflate(
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), null, false);
        initElonezet(edzesTerv, binding.tervElonezetLinearLayout);
        new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public ArrayAdapter<EdzesTerv> getTervListAdapter(Context context, List<EdzesTerv> edzesTervs) {
        return new ArrayAdapter<EdzesTerv>(context, R.layout.tevekenyseg_edzesterv_valaszto_dialog_item, edzesTervs) {
            TevekenysegEdzestervValasztoDialogItemBinding binding;

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.tevekenyseg_edzesterv_valaszto_dialog_item, parent, false);
                    binding = DataBindingUtil.bind(convertView);
                    convertView.setTag(binding);
                } else {
                    binding = (TevekenysegEdzestervValasztoDialogItemBinding) convertView.getTag();
                }

                EdzesTerv item = getItem(position);
                binding.tevekenysegTervValasztoMegnevezes.setText(item.getMegnevezes());
                binding.tevekenysegTervValasztoReszletek.setText(getReszletek(item));
                return binding.getRoot();
            }

            private String getReszletek(EdzesTerv edzesTerv) {
                StringBuilder sb = new StringBuilder();
                for (Edzesnap edzesnap : edzesTerv.getEdzesnapList()) {
                    sb.append(edzesnap.getEdzesNapLabel()).append(" ");
                    String csoportok = edzesnap.getValasztottCsoport().stream().map(Csoport::getIzomcsoport).collect(Collectors.joining(","));
                    if (!edzesnap.getEdzesNapLabel().contains("pihenőnap"))
                        sb.append(": ").append(csoportok).append(" ");

                    sb.append("\n");
                }
                return sb.toString();
            }
        };
    }

    public LiveData<EdzesTerv> getEdzesTervById(LifecycleOwner owner, int id) {
        MutableLiveData<EdzesTerv> liveData = new MutableLiveData<>();
        edzesTervViewModel.getEdzesTervById(id).observe(owner, edzesTervWithEdzesnaps -> {
            if (edzesTervWithEdzesnaps != null) {
                liveData.postValue(edzesTervKeszito.makeEdzesterv(Stream.of(edzesTervWithEdzesnaps).collect(Collectors.toList())).get(0));
            }
        });
        return liveData;
    }

    private void initElonezet(EdzesTerv edzesTerv, LinearLayout layout) {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llparam.topMargin = 20;
        llparam.bottomMargin = 20;
        linearLayout.setLayoutParams(llparam);
        linearLayout.setBackgroundResource(R.drawable.custom_edzesterv_hatter);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        List<Edzesnap> edzesnapList = edzesTerv.getEdzesnapList();

        MvvmEdzestervTitleDialogLayoutBinding titleLayoutBinding =
                MvvmEdzestervTitleDialogLayoutBinding.inflate(LayoutInflater.from(context), null, false);
        titleLayoutBinding.etTitleLabel.setText(edzesTerv.getMegnevezes());
        linearLayout.addView(titleLayoutBinding.getRoot());

        for (Edzesnap edzesnap : edzesnapList) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 20;
            params.bottomMargin = 20;
            params.leftMargin = 10;

            TextView textView = new TextView(context);
            textView.setLayoutParams(params);
            textView.setText(edzesnap.getEdzesNapLabel());
            textView.setPadding(10, 10, 10, 10);
            textView.setBackgroundResource(R.drawable.custom_et_gyak_hatter);
//                binding.tervElonezetLinearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);

            for (Csoport csoport : edzesnap.getValasztottCsoport()) {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                params.bottomMargin = 20;
                params.leftMargin = 70;

                TextView textView1 = new TextView(context);
                textView1.setLayoutParams(params);
                textView1.setText(csoport.getIzomcsoport());
                linearLayout.addView(textView1);

                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                params.bottomMargin = 20;
                params.leftMargin = 100;
                for (GyakorlatTerv gyakorlatTerv : csoport.getValasztottGyakorlatok()) {
                    TextView textView2 = new TextView(context);
                    textView2.setLayoutParams(params);
                    textView2.setText(gyakorlatTerv.toString());

                    linearLayout.addView(textView2);
                }
            }
        }

        layout.addView(linearLayout);
    }
}
