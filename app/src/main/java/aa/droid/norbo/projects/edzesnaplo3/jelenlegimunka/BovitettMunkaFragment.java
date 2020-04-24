package aa.droid.norbo.projects.edzesnaplo3.jelenlegimunka;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.JelenlegiEdzesFragment;
import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.ui.main.SectionsPagerAdapter;

public class BovitettMunkaFragment extends Fragment {
    private FragmentActivity mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        this.mContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bovitett_munka_fragment, container, false);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(mContext.getApplicationContext(), mContext.getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new JelenlegiEdzesFragment(), "Most");
        sectionsPagerAdapter.addFragment(new BovitettKorabbiEdzesFragmant(), "Volt");
        ViewPager viewPager = view.findViewById(R.id.bovitettMunkaViewpager);
        if(viewPager != null) {
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabLayout = view.findViewById(R.id.bovitettMunkaTabs);
            tabLayout.setupWithViewPager(viewPager);
        }

        return view;
    }

    public void adat(Naplo naplo) {
        List<Fragment> fragments = mContext.getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof JelenlegiEdzesFragment) {
                ((JelenlegiEdzesFragment) fragments.get(i)).updateNaploAdat(naplo);
            }
        }
    }

    public void adatGyakorlat(Gyakorlat gyakorlat) {
        List<Fragment> fragments = mContext.getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof BovitettKorabbiEdzesFragmant) {
                ((BovitettKorabbiEdzesFragmant) fragments.get(i)).setListViewAdapter(gyakorlat);
            }
        }
    }
}
