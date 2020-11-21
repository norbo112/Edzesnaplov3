package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import aa.droid.norbo.projects.edzesnaplo3.R;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    protected final static String VALASZTOTT_TERV_ID = "választott terv";
    protected final static String VALASZTOTT_TERV_MEGNEVEZES = "választott terv megnevezés";

    protected T binding;
    private int layoutResource;

    public BaseActivity(int layoutResource) {
        this.layoutResource = layoutResource;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResource, null, false);
        if (getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(binding.getRoot());
        setupCustomActionBar();
    }

    @SuppressLint("RestrictedApi")
    protected PopupMenu showMoreOptionsPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.mvvm_tevekenyseg_more_options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            onContextItemSelected(item);
            return true;
        });

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this, (MenuBuilder) popupMenu.getMenu(), view);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
        return popupMenu;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    public abstract void setupCustomActionBar();
}
