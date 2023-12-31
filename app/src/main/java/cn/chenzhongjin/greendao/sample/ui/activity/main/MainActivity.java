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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dreamliner.lib.frame.base.BaseCompatActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
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

    private List<Order> wd404, wd1514, yh2303, yh2311, lc2402, lc2902;


    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        orderDao = DaoUtil.INSTANCE.getDaoSession().getOrderDao();
        initTimePicker();
        setTime(System.currentTimeMillis());
        mBinding.tvTime.setText(TimeUtil.formateDate(System.currentTimeMillis()) + "预约情况");
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

        mBinding.rvWd404.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mBinding.rvWd1514.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mBinding.rvYh2303.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mBinding.rvYh2311.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mBinding.rvLc2402.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mBinding.rvLc2902.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllDataByLocal();
    }

    private void loadAllDataByLocal() {

        wd404 = new ArrayList<>();
        wd1514 = new ArrayList<>();
        yh2303 = new ArrayList<>();
        yh2311 = new ArrayList<>();
        lc2402 = new ArrayList<>();
        lc2902 = new ArrayList<>();

        Observable.just("")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> {
                            QueryBuilder<Order> builder = orderDao.queryBuilder();
                            return Observable.just(

                                    builder.whereOr(builder.and(OrderDao.Properties.StartTime.ge(queryStartTime), OrderDao.Properties.StartTime.le(queryEndTime))
                                                    , builder.and(OrderDao.Properties.StartTime.le(queryStartTime), OrderDao.Properties.EndTime.ge(queryEndTime))
                                                    , builder.and(OrderDao.Properties.EndTime.ge(queryStartTime), OrderDao.Properties.EndTime.le(queryEndTime)
                                                    ))
                                            .orderAsc(OrderDao.Properties.StartTime).list());
                        }
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
                                        wd404.add(order);
                                    } else if (Objects.equals(order.getAddress(), "万达1514")) {
                                        wd1514.add(order);
                                    } else if (Objects.equals(order.getAddress(), "银河大厦2303")) {
                                        yh2303.add(order);
                                    } else if (Objects.equals(order.getAddress(), "银河大厦2311")) {
                                        yh2311.add(order);
                                    } else if (Objects.equals(order.getAddress(), "吕厝304栋2402—888")) {
                                        lc2402.add(order);
                                    } else if (Objects.equals(order.getAddress(), "吕厝302栋2902—02")) {
                                        lc2902.add(order);
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
                        DetailAdapter wd404Adapter = new DetailAdapter(wd404, MainActivity.this);
                        mBinding.rvWd404.setAdapter(wd404Adapter);

                        DetailAdapter wd1514Adapter = new DetailAdapter(wd1514, MainActivity.this);
                        mBinding.rvWd1514.setAdapter(wd1514Adapter);

                        DetailAdapter yh2303Adapter = new DetailAdapter(yh2303, MainActivity.this);
                        mBinding.rvYh2303.setAdapter(yh2303Adapter);

                        DetailAdapter yh2311Adapter = new DetailAdapter(yh2311, MainActivity.this);
                        mBinding.rvYh2311.setAdapter(yh2311Adapter);

                        DetailAdapter lc2402Adapter = new DetailAdapter(lc2402, MainActivity.this);
                        mBinding.rvLc2402.setAdapter(lc2402Adapter);

                        DetailAdapter lc2902Adapter = new DetailAdapter(lc2902, MainActivity.this);
                        mBinding.rvLc2902.setAdapter(lc2902Adapter);


                    }
                });
    }


    private void initTimePicker() {//Dialog 模式下，在底部弹出

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                setTime(date.getTime());
                mBinding.tvTime.setText(TimeUtil.formateDate(date) + "预约情况");
                loadAllDataByLocal();
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
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
        time = time - 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        queryStartTime = date.getTime() / 1000 * 1000;


        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        date = calendar.getTime();
        queryEndTime = date.getTime() / 1000 * 1000;

        System.out.println("time queryStartTime " + TimeUtil.formateDateMMDDHH(queryStartTime) + ", queryEndTime " + TimeUtil.formateDateMMDDHH(queryEndTime));
    }
}
