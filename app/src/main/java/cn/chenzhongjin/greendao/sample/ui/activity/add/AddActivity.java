package cn.chenzhongjin.greendao.sample.ui.activity.add;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dreamliner.lib.frame.base.BaseCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.chenzhongjin.greendao.sample.AppContext;
import cn.chenzhongjin.greendao.sample.R;
import cn.chenzhongjin.greendao.sample.database.Order;
import cn.chenzhongjin.greendao.sample.database.OrderDao;
import cn.chenzhongjin.greendao.sample.databinding.ActAddBinding;
import cn.chenzhongjin.greendao.sample.eventbus.UserChangeEvent;
import cn.chenzhongjin.greendao.sample.utils.DaoUtil;
import cn.chenzhongjin.greendao.sample.utils.TimeUtil;


/**
 * @author: chenzj
 * @Title: MainActivity
 * @Description:
 * @date: 2016/3/24 22:45
 * @email: admin@chenzhongjin.cn
 */
public class AddActivity extends BaseCompatActivity {

    protected ActAddBinding mBinding;
    public OrderDao orderDao;
    private Order order = new Order();

    private TimePickerView startPvTime, endPvTime;

    private long startTime = System.currentTimeMillis();

    private String startTimeStr = TimeUtil.formateDateMMDDHH(startTime);

    private long endTime = startTime + 1000 * 60 * 60 * 2;

    private String address;

    private String endTimeStr = TimeUtil.formateDateMMDDHH(endTime);


    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_add);
        orderDao = DaoUtil.INSTANCE.getDaoSession().getOrderDao();
        initEndTimePicker();
        initStartTimePicker();
        mBinding.tvStart.setText(startTimeStr);
        mBinding.tvEnd.setText(endTimeStr);


        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mBinding.address.getText())) {
                    showToast("请输入房间号");
                    return;
                }
                order.setStartTime(startTime);
                order.setEndTime(endTime);
                order.setAddress(address);


                AppContext.selectOrder = order;
                AddDialog dialog = new AddDialog();
                dialog.okListener = new AddDialog.OkListener() {
                    @Override
                    public void onOK() {
                        insert();
                    }
                };
                dialog.show(getSupportFragmentManager(), "");
            }
        });

        mBinding.tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endPvTime.show();
            }
        });

        mBinding.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPvTime.show();
            }
        });

        mBinding.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressDialog dialog = new AddressDialog();
                dialog.selectListener = new AddressDialog.SelectListener() {
                    @Override
                    public void onSelect(String a) {
                        address = a;
                        mBinding.address.setText(address);
                    }
                };
                dialog.show(getSupportFragmentManager(), "");
            }
        });

    }


    private void initStartTimePicker() {//Dialog 模式下，在底部弹出
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTime));
        startPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                startTimeStr = getTime(date);
                startTime = date.getTime();
                mBinding.tvStart.setText(startTimeStr);

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .setDate(calendar)
                .build();

        Dialog mDialog = startPvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            startPvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private void initEndTimePicker() {//Dialog 模式下，在底部弹出

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(endTime));
        endPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                endTimeStr = getTime(date);
                endTime = date.getTime();
                mBinding.tvEnd.setText(endTimeStr);

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .setDate(calendar)
                .build();

        Dialog mDialog = endPvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            endPvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private void insert() {

        orderDao.insert(order);
        postEvent(new UserChangeEvent());
        finish();
//        pop();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH时mm分");
        return format.format(date);
    }
}
