package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import aa.droid.norbo.projects.edzesnaplo3.R;

public class EdzesTervBaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    protected T binding;
    private int layoutId;

    public EdzesTervBaseActivity(int contentLayoutId) {
        super(contentLayoutId);
        this.layoutId = contentLayoutId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutId, null, false);
        if (getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(binding.getRoot());
    }
}
