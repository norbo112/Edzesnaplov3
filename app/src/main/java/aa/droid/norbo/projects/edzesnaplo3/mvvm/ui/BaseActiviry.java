package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

public abstract class BaseActiviry<T extends ViewDataBinding> extends AppCompatActivity {
    protected T binding;
    private int layoutResource;

    public BaseActiviry(int layoutResource) {
        this.layoutResource = layoutResource;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResource, null, false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(binding.getRoot());
    }

    @SuppressLint("RestrictedApi")
    protected void showMoreOptionsPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.mvvm_tevekenyseg_more_options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            onContextItemSelected(item);
            return true;
        });

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this, (MenuBuilder) popupMenu.getMenu(), view);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.tevekenyseg_more_options_exit) {
            kilepes();
        } else if(item.getItemId() == R.id.tevekenyseg_naplo_view) {
            Toast.makeText(this, "Naplók megtekintése...", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    public abstract void setupCustomActionBar();

    public void kilepes() {
        new AlertDialog.Builder(this)
                .setMessage("Biztosan ki akarsz lépni?")
                .setPositiveButton("ok", (dialog, which) -> {
                    Process.killProcess(Process.myPid());
                })
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
