package cn.chenzhongjin.greendao.sample.eventbus;

import java.util.List;

import cn.chenzhongjin.greendao.sample.database.Order;

public class SelectEvent {

    private List<Order> mUsers;

    public SelectEvent(List<Order> users) {
        mUsers = users;
    }

    public List<Order> getUsers() {
        return mUsers;
    }
}
