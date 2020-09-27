package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> myFragments;
    private List<String> myFragmentTitles;
    private final Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        myFragments = new ArrayList<>();
        myFragmentTitles = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        myFragments.add(fragment);
        myFragmentTitles.add(title);
    }

    public void setPageTitle(int position, String newTitle) {
        myFragmentTitles.set(position, newTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return myFragmentTitles.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }
}