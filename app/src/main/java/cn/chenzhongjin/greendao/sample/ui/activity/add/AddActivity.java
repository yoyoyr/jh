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

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import cn.chenzhongjin.greendao.sample.AppContext;
import cn.chenzhongjin.greendao.sample.R;
import cn.chenzhongjin.greendao.sample.database.Order;
import cn.chenzhongjin.greendao.sample.database.OrderDao;
import cn.chenzhongjin.greendao.sample.databinding.ActAddBinding;
import cn.chenzhongjin.greendao.sample.eventbus.UserChangeEvent;
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
                    new ErrorDialog("请输入房间号").show(getSupportFragmentManager(), "");
                    return;
                }

                if (startTime >= endTime) {
                    new ErrorDialog("开始时间" + TimeUtil.formateDateHHmm(startTime) + "不能大于结束时间" + TimeUtil.formateDateHHmm(endTime)).show(getSupportFragmentManager(), "");
                    return;
                }
                order.setStartTime(startTime);
                order.setEndTime(endTime);
                order.setAddress(address);

                Observable.just("")
                        .subscribeOn(Schedulers.io())
                        .flatMap(s -> {
                                    //对象错误
                                    QueryBuilder<Order> builder = orderDao.queryBuilder();
                                    return Observable.just(
                                            builder.whereOr(builder.and(OrderDao.Properties.StartTime.ge(startTime), OrderDao.Properties.StartTime.le(endTime))
                                                            , builder.and(OrderDao.Properties.StartTime.le(startTime), OrderDao.Properties.EndTime.ge(endTime))
                                                            , builder.and(OrderDao.Properties.EndTime.ge(startTime), OrderDao.Properties.EndTime.le(endTime)
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
                                    for (int i = 0; i < users.size(); ++i) {
                                        Order o = users.get(i);
                                        if (Objects.equals(order.getAddress(), o.getAddress())) {
                                            new ErrorDialog(o.getAddress() + "   " + TimeUtil.formateDateHHmm(o.getStartTime()) + "到" + TimeUtil.formateDateHHmm(o.getEndTime()) + "已被预约").show(getSupportFragmentManager(), "");
                                            return;
                                        }
                                    }

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
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        });

        mBinding.tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(endTime));
                endPvTime.setDate(calendar);
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

    private boolean isFirst = true;


    private void initStartTimePicker() {//Dialog 模式下，在底部弹出
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTime));
        startPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                startTimeStr = getTime(date);
                startTime = date.getTime();
                mBinding.tvStart.setText(startTimeStr);
                if (isFirst) {
                    endTime = startTime + 1000 * 60 * 60 * 2;
                    endTimeStr = getTime(new Date(endTime));
                    mBinding.tvEnd.setText(endTimeStr);
                    isFirst = false;
                }
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
                .isCyclic(true)
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
                .isCyclic(true)
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
