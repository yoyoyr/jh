package cn.chenzhongjin.greendao.sample.ui.activity.main;

import android.view.View;

import com.dreamliner.lib.frame.base.BaseCompatActivity;
import com.dreamliner.rvhelper.interfaces.OnItemClickListener;
import com.dreamliner.rvhelper.interfaces.OnItemLongClickListener;

import cn.chenzhongjin.greendao.sample.database.Order;

public class UpdateUtil implements OnItemClickListener<Order>, OnItemLongClickListener<Order> {

    protected BaseCompatActivity mActivity;

    public UpdateUtil(BaseCompatActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onItemClick(View v, int position, Order user) {
    }

    @Override
    public boolean onItemLongClick(View v, int position, Order user) {
        return false;
    }
}

