package com.faraway.auditall.DataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;

/**
 * @author Fan
 * @description审核基本信息数据库
 * @date 2019-09-10
 * @time 10:32
 **/
@Entity
public class BasicInfo {

    @Id(autoincrement = true)
    private Long id;

    private Date date;

    private String time;

    private String auditor;

    private String area;

    private String auditTarget;

    private String auditContent;

    private long totalYesNumber;

    private long totalNoNumber;

    private int auditItemNum;

    private String proceed;

    @ToMany(referencedJoinProperty = "idAuditItem")
    List<AuditItem> auditItems;


    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAuditItem() {
        return auditItem;
    }

    public void setAuditItem(String auditItem) {
        this.auditItem = auditItem;
    }

    public long getTotalYesNumber() {
        return this.totalYesNumber;
    }

    public void setTotalYesNumber(long totalYesNumber) {
        this.totalYesNumber = totalYesNumber;
    }

    public long getTotalNoNumber() {
        return this.totalNoNumber;
    }

    public void setTotalNoNumber(long totalNoNumber) {
        this.totalNoNumber = totalNoNumber;
    }

    public String getProceed() {
        return this.proceed;
    }

    public void setProceed(String proceed) {
        this.proceed = proceed;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 112916245)
    public List<AuditItem> getAuditItems() {
        if (auditItems == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AuditItemDao targetDao = daoSession.getAuditItemDao();
            List<AuditItem> auditItemsNew = targetDao
                    ._queryBasicInfo_AuditItems(id);
            synchronized (this) {
                if (auditItems == null) {
                    auditItems = auditItemsNew;
                }
            }
        }
        return auditItems;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1281586482)
    public synchronized void resetAuditItems() {
        auditItems = null;
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

    public String getAuditTarget() {
        return this.auditTarget;
    }

    public void setAuditTarget(String auditTarget) {
        this.auditTarget = auditTarget;
    }

    public String getAuditContent() {
        return this.auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAuditItemNum() {
        return this.auditItemNum;
    }

    public void setAuditItemNum(int auditItemNum) {
        this.auditItemNum = auditItemNum;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1606283967)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBasicInfoDao() : null;
    }

    private String auditItem;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 595705606)
    private transient BasicInfoDao myDao;

    @Generated(hash = 1778811284)
    public BasicInfo(Long id, Date date, String time, String auditor, String area, String auditTarget,
            String auditContent, long totalYesNumber, long totalNoNumber, int auditItemNum,
            String proceed, String auditItem) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.auditor = auditor;
        this.area = area;
        this.auditTarget = auditTarget;
        this.auditContent = auditContent;
        this.totalYesNumber = totalYesNumber;
        this.totalNoNumber = totalNoNumber;
        this.auditItemNum = auditItemNum;
        this.proceed = proceed;
        this.auditItem = auditItem;
    }

    @Generated(hash = 1912859097)
    public BasicInfo() {
    }

}
