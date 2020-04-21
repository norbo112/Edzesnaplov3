package aa.droid.norbo.projects.edzesnaplo3.ui.controller;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import aa.droid.norbo.projects.edzesnaplo3.Edzes;
import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.ui.controller.interfaces.EdzesFragmentControllerInterface;

public class EdzesFragmentCntImpl implements EdzesFragmentControllerInterface {

    @SuppressLint("SetTextI18n")
    @Override
    public void prepareGyakorlat(Fragment fragment, View view) {
        Edzes edzes = (Edzes) fragment;
        enableButtons(edzes, view);
        edzes.getSorozats().clear();
        edzes.getListAdapter().notifyDataSetChanged();
        edzes.getStopperHandler().removeCallbacks(edzes.getStopperTimer());

        SeekBar seekBarsuly = view.findViewById(R.id.seekBarsuly);
        SeekBar seekBarism = view.findViewById(R.id.seekBarism);

        if(seekBarsuly != null) {
            seekBarsuly.setProgress(0);
            seekBarism.setProgress(0);
        } else {
            ((TextView) view.findViewById(R.id.etSuly)).setText("");
            ((TextView) view.findViewById(R.id.etIsm)).setText("");
        }

        ((TextView)view.findViewById(R.id.tvStopper)).setText("00:00");
        if(edzes.getGyakorlat() != null) {
            ((TextView) view.findViewById(R.id.gyak_title)).setText(edzes.getGyakorlat().getMegnevezes() + " használata");
        } else {
            ((TextView)view.findViewById(R.id.gyak_title)).setText("Kérlek válassz egy gyakorlatot");
        }
    }

    @Override
    public void disableButtons(Fragment fragment, View view) {
        ((TextView)view.findViewById(R.id.gyak_title)).setText("Kérlek válassz egy gyakorlatot");
        ((TextView)view.findViewById(R.id.gyak_title)).setTextColor(Color.RED);

        if(view.findViewById(R.id.seekBarsuly) != null) {
            view.findViewById(R.id.seekBarsuly).setEnabled(false);
            view.findViewById(R.id.seekBarism).setEnabled(false);
            view.findViewById(R.id.tvSulyLabel).setEnabled(false);
            view.findViewById(R.id.tvIsmLabel).setEnabled(false);
        } else {
            view.findViewById(R.id.etIsm).setEnabled(false);
            view.findViewById(R.id.etSuly).setEnabled((false));
        }
        view.findViewById(R.id.btnEdzesSave).setEnabled(false);
        view.findViewById(R.id.btnSorozatAdd).setEnabled(false);
        view.findViewById(R.id.btnEdzesUjGy).setEnabled(false);
    }

    @Override
    public void enableButtons(Fragment fragment, View view) {
        Edzes edzes = (Edzes) fragment;
        ((TextView)view.findViewById(R.id.gyak_title)).setText(String.format("%s használata",
                edzes.getGyakorlat().getMegnevezes()));
        ((TextView)view.findViewById(R.id.gyak_title)).setTextColor(Color.WHITE);

        if(view.findViewById(R.id.seekBarsuly) != null) {
            view.findViewById(R.id.seekBarsuly).setEnabled(true);
            view.findViewById(R.id.seekBarism).setEnabled(true);
            view.findViewById(R.id.tvSulyLabel).setEnabled(true);
            view.findViewById(R.id.tvIsmLabel).setEnabled(true);
        } else {
            view.findViewById(R.id.etIsm).setEnabled(true);
            view.findViewById(R.id.etSuly).setEnabled((true));
        }
        view.findViewById(R.id.btnEdzesSave).setEnabled(true);
        view.findViewById(R.id.btnSorozatAdd).setEnabled(true);
        view.findViewById(R.id.btnEdzesUjGy).setEnabled(true);
    }
}
