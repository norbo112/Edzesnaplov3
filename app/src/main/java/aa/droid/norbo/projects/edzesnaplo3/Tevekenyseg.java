package aa.droid.norbo.projects.edzesnaplo3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.datainterfaces.AdatBeallitoInterface;
import aa.droid.norbo.projects.edzesnaplo3.ui.main.SectionsPagerAdapter;

public class Tevekenyseg extends AppCompatActivity implements AdatBeallitoInterface {
    private final String TAG = getClass().getSimpleName();
    private String felhasznalonev;
    private ListView gyakorlatListView;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_teveknyseg);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tevékenység");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);

        felhasznalonev = getIntent().getStringExtra(MainActivity.FELHASZNALONEV);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.addFraggment(new GyakorlatValaszto(), "Gyakorlat");
        sectionsPagerAdapter.addFraggment(new Edzes(), "Edzés");
        viewPager = findViewById(R.id.view_pager);
        if(viewPager != null) {
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tevekenyseg, menu);
        menu.removeItem(R.id.app_bar_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mentett_nezet :
                startActivity(new Intent(this, MentettNaploActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void adatGyakorlat(Gyakorlat gyakorlat) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if(fragments.get(i) instanceof Edzes) {
                ((Edzes)fragments.get(i)).setGyakorlat(gyakorlat);
            }
        }
    }

    @Override
    public void adatNaplo(Naplo naplo) {

    }

    @Override
    public String getFelhasznaloNev() {
        return felhasznalonev;
    }
}