package cn.chenzhongjin.greendao.sample.ui.activity.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import cn.chenzhongjin.greendao.sample.R;
import cn.chenzhongjin.greendao.sample.database.Order;
import cn.chenzhongjin.greendao.sample.database.OrderDao;
import cn.chenzhongjin.greendao.sample.databinding.ActMainBinding;
import cn.chenzhongjin.greendao.sample.ui.activity.add.AddActivity;
import cn.chenzhongjin.greendao.sample.utils.DaoUtil;
import cn.chenzhongjin.greendao.sample.utils.TimeUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * @author: chenzj
 * @Title: MainActivity
 * @Description:
 * @date: 2016/3/24 22:45
 * @email: admin@chenzhongjin.cn
 */
public class MainActivity extends BaseCompatActivity {

    protected ActMainBinding mBinding;
    public OrderDao orderDao;
    private TimePickerView pvTime;

    private BaseCompatActivity main = this;

    private long queryStartTime, queryEndTime;

    private StringBuffer wd404, wd1514, yh2303, yh2311 = new StringBuffer();


    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        orderDao = DaoUtil.INSTANCE.getDaoSession().getOrderDao();
        initTimePicker();
        setTime(System.currentTimeMillis());
        mBinding.tvTime.setText(TimeUtil.formateDate(System.currentTimeMillis()));
        mBinding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
        mBinding.tvAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllDataByLocal();
    }

    private void loadAllDataByLocal() {

        wd404 = new StringBuffer("万达404 ： ");
        wd1514 = new StringBuffer("万达1514 ： ");
        yh2303 = new StringBuffer("银河大厦2303 ： ");
        yh2311 = new StringBuffer("银河大厦2311 ： ");

        Observable.just("")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> Observable.just(orderDao.queryBuilder()
                        .whereOr(OrderDao.Properties.StartTime.ge(queryStartTime), OrderDao.Properties.EndTime.ge(queryStartTime))
                        .orderAsc(OrderDao.Properties.StartTime).list())
                )
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new DisposableObserver<List<Order>>() {
                    @Override
                    public void onNext(List<Order> users) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            users.forEach(new Consumer<Order>() {
                                @Override
                                public void accept(Order order) {
                                    if (Objects.equals(order.getAddress(), "万达404")) {
                                        wd404.append("\n        ").append(TimeUtil.getTimeScope(order));
                                    } else if (Objects.equals(order.getAddress(), "万达1514")) {
                                        wd1514.append("\n        ").append(TimeUtil.getTimeScope(order));
                                    } else if (Objects.equals(order.getAddress(), "银河大厦2303")) {
                                        yh2303.append("\n        ").append(TimeUtil.getTimeScope(order));
                                    } else if (Objects.equals(order.getAddress(), "银河大厦2311")) {
                                        yh2311.append("\n        ").append(TimeUtil.getTimeScope(order));
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mBinding.wd404.setText(wd404.toString());
                        mBinding.wd1514.setText(wd1514.toString());
                        mBinding.yh2303.setText(yh2303.toString());
                        mBinding.yh2311.setText(yh2311.toString());

                    }
                });
    }


    private void initTimePicker() {//Dialog 模式下，在底部弹出

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mBinding.tvTime.setText(TimeUtil.formateDate(date));
                loadAllDataByLocal();
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        setTime(date.getTime());
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .isCyclic(true)
                .setDate(calendar)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private void setTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date date = calendar.getTime();
        queryStartTime = date.getTime() / 1000 * 1000;


        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MINUTE, 999);
        date = calendar.getTime();
        queryEndTime = date.getTime() / 1000 * 1000;
    }
}
