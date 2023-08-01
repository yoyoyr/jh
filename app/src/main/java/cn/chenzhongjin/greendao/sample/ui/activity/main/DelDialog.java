package cn.chenzhongjin.greendao.sample.ui.activity.main;

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

import cn.chenzhongjin.greendao.sample.AppContext;
import cn.chenzhongjin.greendao.sample.R;
import cn.chenzhongjin.greendao.sample.databinding.DialogAddBinding;
import cn.chenzhongjin.greendao.sample.databinding.DialogDelBinding;
import cn.chenzhongjin.greendao.sample.utils.TimeUtil;

public class DelDialog extends DialogFragment {

    public OkListener okListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DialogDelBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_del, container, false);

        binding.tvAddress.setText("门店 ： " + AppContext.selectOrder.getAddress());
        binding.tvTime.setText("时间 ： " + TimeUtil.formateDateHHmm(AppContext.selectOrder.getStartTime()) + " - " + TimeUtil.formateDateHHmm(AppContext.selectOrder.getEndTime()));

        binding.tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okListener.onOK();
                dismissAllowingStateLoss();
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
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

        setCancelable(false);


    }


    private int getDp(int dpVal) {
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                Resources.getSystem().getDisplayMetrics()
        ) + 0.5f);
    }

    public interface OkListener {
        void onOK();
    }
}
