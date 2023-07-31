package cn.chenzhongjin.greendao.sample.database;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import java.io.Serializable;

import cn.chenzhongjin.greendao.sample.utils.TimeUtil;

@Entity(active = true, nameInDb = "Order")
public class Order implements Serializable {

    private static final long serialVersionUID = -3508558775915557595L;

    @Id(autoincrement = true )
    private Long id;

    //地址
    private String address;

    //    开始时间
    private Long startTime;

    //    结束时间
    private Long endTime;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 949219203)
    private transient OrderDao myDao;


    @Generated(hash = 1524209028)
    public Order(Long id, String address, Long startTime, Long endTime) {
        this.id = id;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    @Generated(hash = 1105174599)
    public Order() {
    }


    public String getTimeFormat() {
        try {

            return "时间 ： " + TimeUtil.formateDateMMDDHH(startTime) + " - " + TimeUtil.formateDateMMDDHH(endTime);
        } catch (Exception ex) {
            return "";
        }
    }


    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 965731666)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
    }

}
