package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatVideoLinkSharedBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.listadapters.GyakorlatItemAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class VideoUtils {
    private static final String TAG = "VideoUtils";
    private GyakorlatViewModel gyakorlatViewModel;
    private ModelConverter modelConverter;
    private Context context;
    private LayoutInflater inflater;

    @Inject
    public VideoUtils(GyakorlatViewModel gyakorlatViewModel, @ActivityContext Context context, ModelConverter modelConverter) {
        this.gyakorlatViewModel = gyakorlatViewModel;
        this.context = context;
        this.modelConverter = modelConverter;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static final String YT_API_KEY = "AIzaSyDzSqMr9tFI2MvQ_b7BMCQE8Xrw3DWvtOw";

    public void createVideoSaveDialog(String aLink, List<GyakorlatUI> gyakorlatUIS, VideoUtilsInterface callback) {
        AlertDialog alertDialog;
        if (gyakorlatUIS != null && gyakorlatUIS.size() > 0) {
            Log.i(TAG, "createVideoSaveDialog: gyakotlaz nem null");
            MvvmGyakorlatVideoLinkSharedBinding binding = MvvmGyakorlatVideoLinkSharedBinding.inflate(inflater);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(binding.getRoot());
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            String link = aLink.substring(aLink.lastIndexOf('/') + 1);
            binding.gyakorlatVideoLink.setText(link);

            GyakorlatItemAdapter adapter = new GyakorlatItemAdapter(gyakorlatUIS, context);
            binding.gyakSpinner.setAdapter(adapter);

            List<String> izomcsoportok = new ArrayList<>();
            izomcsoportok.add("Válassz...");
            izomcsoportok.addAll(gyakorlatUIS.stream().map(GyakorlatUI::getCsoport).distinct().collect(Collectors.toList()));
            ArrayAdapter<String> izomcsoportokAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, izomcsoportok);
            izomcsoportokAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            binding.spinIzomcsop.setAdapter(izomcsoportokAdapter);
            binding.spinIzomcsop.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Filter filter = ((GyakorlatItemAdapter) binding.gyakSpinner.getAdapter()).getFilter();
                            if (filter != null) {
                                String constraint = parent.getAdapter().getItem(position).toString();
                                if(constraint.equals("Válassz...")) constraint = "";
                                filter.filter(constraint);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    }
            );

            binding.gyakorlatVideoBtnMentesMegse.setOnClickListener(v -> callback.finishActivity());
            binding.gyakorlatVideoBtnMentes.setOnClickListener(v -> {
                GyakorlatUI gyakorlatUI = (GyakorlatUI) binding.gyakSpinner.getSelectedItem();
                if (gyakorlatUI.getVideolink() != null && gyakorlatUI.getVideolink().length() > 1) {
                    Toast.makeText(context, "Ehhez a gyakorlathoz már van mentve videó link!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(binding.gyakorlatVideoLink.getText().toString()) && binding.gyakorlatVideoLink.getText().toString().length() > 5) {
                    gyakorlatUI.setVideolink(binding.gyakorlatVideoLink.getText().toString());
                } else {
                    Toast.makeText(context, "Videó azonosító hiányzik", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(binding.gyakorlatVideoLinkPoz.getText().toString())) {
                    gyakorlatUI.setVideostartpoz(binding.gyakorlatVideoLinkPoz.getText().toString());
                } else {
                    gyakorlatUI.setVideostartpoz("0");
                }

                gyakorlatViewModel.update(modelConverter.fromUI(gyakorlatUI));
                Toast.makeText(context, "Gyakorlat videó rögzítve!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            });

            alertDialog.show();
        } else {
            Toast.makeText(context, "Nincs gyakorlat rögzítve amihez hozzálehetne adni a videót", Toast.LENGTH_SHORT).show();
        }
    }
}
