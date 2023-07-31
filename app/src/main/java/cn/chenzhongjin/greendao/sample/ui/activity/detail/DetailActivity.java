package cn.chenzhongjin.greendao.sample.ui.activity.detail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.dreamliner.lib.frame.base.BaseCompatActivity;

import cn.chenzhongjin.greendao.sample.AppContext;
import cn.chenzhongjin.greendao.sample.R;
import cn.chenzhongjin.greendao.sample.databinding.ActDetailBinding;
import cn.chenzhongjin.greendao.sample.utils.TimeUtil;


/**
 * @author: chenzj
 * @Title: MainActivity
 * @Description:
 * @date: 2016/3/24 22:45
 * @email: admin@chenzhongjin.cn
 */
public class DetailActivity extends BaseCompatActivity {

    protected ActDetailBinding mBinding;

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_detail);
        mBinding.tvStart.setText(TimeUtil.formateDateMMDDHH(AppContext.selectOrder.getStartTime()));
        mBinding.tvEnd.setText(TimeUtil.formateDateMMDDHH(AppContext.selectOrder.getEndTime()));
        mBinding.addresss.setText(AppContext.selectOrder.getAddress());
    }

}
