package cn.chenzhongjin.greendao.sample.ui.activity.add;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.dreamliner.lib.util.ScreenUtils;

import cn.chenzhongjin.greendao.sample.R;
import cn.chenzhongjin.greendao.sample.databinding.DialogAddressBinding;
import cn.chenzhongjin.greendao.sample.databinding.DialogErrorBinding;

public class ErrorDialog extends DialogFragment {

    private String msg;
    public ErrorDialog(String m){
        msg = m;
    }

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DialogErrorBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_error, container, false);

        binding.tvError.setText(msg);

        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        if (params != null) {
            params.width = ScreenUtils.getScreenWidth() - (2 * getDp(52));
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            getDialog().getWindow().setAttributes(params);
        }

        setCancelable(true);

    }


    private int getDp(int dpVal) {
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                Resources.getSystem().getDisplayMetrics()
        ) + 0.5f);
    }

    public interface SelectListener{
        void onSelect(String address);
    }
}
