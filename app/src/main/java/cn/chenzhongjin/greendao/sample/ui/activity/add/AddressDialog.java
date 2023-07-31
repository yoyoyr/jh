package cn.chenzhongjin.greendao.sample.ui.activity.add;

import android.app.Dialog;
import android.content.Context;
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

public class AddressDialog extends DialogFragment {

    public SelectListener selectListener;


    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DialogAddressBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_address, container, false);

        binding.tv404.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectListener !=null){
                    selectListener.onSelect("万达404");
                }
                dismissAllowingStateLoss();
            }
        });
        binding.tv1514.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectListener !=null){
                    selectListener.onSelect("万达1514");
                }
                dismissAllowingStateLoss();
            }
        });
        binding.tv2303.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectListener !=null){
                    selectListener.onSelect("银河大厦2303");
                }
                dismissAllowingStateLoss();
            }
        });
        binding.tv2311.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectListener !=null){
                    selectListener.onSelect("银河大厦2311");
                }
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

    public interface SelectListener{
        void onSelect(String address);
    }
}
