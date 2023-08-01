package cn.chenzhongjin.greendao.sample.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.dreamliner.lib.frame.base.BaseCompatActivity;
import java.util.List;
import cn.chenzhongjin.greendao.sample.AppContext;
import cn.chenzhongjin.greendao.sample.R;
import cn.chenzhongjin.greendao.sample.database.Order;
import cn.chenzhongjin.greendao.sample.database.OrderDao;
import cn.chenzhongjin.greendao.sample.databinding.ItemDetailBinding;
import cn.chenzhongjin.greendao.sample.ui.activity.add.AddActivity;
import cn.chenzhongjin.greendao.sample.utils.DaoUtil;
import cn.chenzhongjin.greendao.sample.utils.TimeUtil;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.VH> {

    private List<Order> datas;

    private OrderDao orderDao;

    private BaseCompatActivity activity;

    public DetailAdapter(List<Order> d, BaseCompatActivity a) {
        datas = d;
        activity = a;
        orderDao = DaoUtil.INSTANCE.getDaoSession().getOrderDao();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        VH vh = new VH(DataBindingUtil.inflate(layoutInflater, R.layout.item_detail, parent, false));
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Order order = datas.get(position);
        holder.binding.tvDetail.setText(TimeUtil.getTimeScope(order));

        holder.binding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddActivity.class);
                intent.putExtra("id", order.getId());
                context.startActivity(intent);
            }
        });

        holder.binding.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DelDialog dialog = new DelDialog();
                AppContext.selectOrder = order;
                dialog.okListener = new DelDialog.OkListener() {
                    @Override
                    public void onOK() {
                        orderDao.delete(order);
                        datas.remove(order);
                        notifyDataSetChanged();
                    }
                };
                dialog.show(activity.getSupportFragmentManager(), "");

            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        public ItemDetailBinding binding;

        public VH(@NonNull ItemDetailBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
