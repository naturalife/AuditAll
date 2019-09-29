package com.faraway.auditall.DataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @author Fan
 * @description
 * @date 2019-09-24
 * @time 9:31
 **/
@Entity
public class AuditInfo {
    @Id
    private Long id;

    private long number;//序号

    private String auditItem;//审核项目

    private long position;

    private String auditResult;//审核结果，符合还是不符合

    private String auditFind;//审核发现文字

    private long yesNumber;//符合项目数

    private long noNumber;//不符合项目数

    @ToMany(referencedJoinProperty = "idAuditPhotos")
    List<AuditPhotos> auditPhotos;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1895609854)
    private transient AuditInfoDao myDao;

    @Generated(hash = 1105125614)
    public AuditInfo(Long id, long number, String auditItem, long position,
            String auditResult, String auditFind, long yesNumber, long noNumber) {
        this.id = id;
        this.number = number;
        this.auditItem = auditItem;
        this.position = position;
        this.auditResult = auditResult;
        this.auditFind = auditFind;
        this.yesNumber = yesNumber;
        this.noNumber = noNumber;
    }

    @Generated(hash = 47619703)
    public AuditInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNumber() {
        return this.number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getAuditItem() {
        return this.auditItem;
    }

    public void setAuditItem(String auditItem) {
        this.auditItem = auditItem;
    }

    public long getPosition() {
        return this.position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getAuditResult() {
        return this.auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditFind() {
        return this.auditFind;
    }

    public void setAuditFind(String auditFind) {
        this.auditFind = auditFind;
    }

    public long getYesNumber() {
        return this.yesNumber;
    }

    public void setYesNumber(long yesNumber) {
        this.yesNumber = yesNumber;
    }

    public long getNoNumber() {
        return this.noNumber;
    }

    public void setNoNumber(long noNumber) {
        this.noNumber = noNumber;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2022040914)
    public List<AuditPhotos> getAuditPhotos() {
        if (auditPhotos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AuditPhotosDao targetDao = daoSession.getAuditPhotosDao();
            List<AuditPhotos> auditPhotosNew = targetDao
                    ._queryAuditInfo_AuditPhotos(id);
            synchronized (this) {
                if (auditPhotos == null) {
                    auditPhotos = auditPhotosNew;
                }
            }
        }
        return auditPhotos;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1203855523)
    public synchronized void resetAuditPhotos() {
        auditPhotos = null;
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
    @Generated(hash = 1854644603)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAuditInfoDao() : null;
    }

}
