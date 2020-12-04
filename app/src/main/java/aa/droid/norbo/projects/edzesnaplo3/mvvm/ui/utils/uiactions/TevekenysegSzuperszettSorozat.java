package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions;

import android.util.Log;

import aa.droid.norbo.projects.edzesnaplo3.databinding.SzuperszettSorozatRogzitoLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.SorozatDisplay;

public class TevekenysegSzuperszettSorozat {
    private static final String TAG = "TevekenysegSzuperszettS";
    private final SzuperszettSorozatRogzitoLayoutBinding binding;
    private final SzettGyakRemover szettGyakRemover;

    public TevekenysegSzuperszettSorozat(SzuperszettSorozatRogzitoLayoutBinding binding, SzettGyakRemover szettGyakRemover) {
        this.binding = binding;
        this.szettGyakRemover = szettGyakRemover;
    }

    public void pluszSzettSorozat() {
        if(binding != null) {
            GyakorlatUI gyakorlatUI = binding.getGyakorlatUI();
            SorozatDisplay sorozatUI = binding.getSorozatUI();
            Log.i(TAG, "pluszSzettSorozat: "+gyakorlatUI.getMegnevezes()+"= suly=" + sorozatUI.getSuly() + ", ism=" + sorozatUI.getIsm());
        }
    }

    public void removeSzettGyakorlat(int viewNumber) {
        szettGyakRemover.removeSzettGyakById(viewNumber);
    }
}
