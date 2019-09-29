package com.faraway.auditall.DataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Fan
 * @description
 * @date 2019-09-21
 * @time 14:23
 **/
@Entity
public class AuditItem {
    @Id(autoincrement = true)
    private Long id;

    private String auditItem;

    private String auditClass;

    private Long idAuditItem;

    @Generated(hash = 484736178)
    public AuditItem(Long id, String auditItem, String auditClass,
            Long idAuditItem) {
        this.id = id;
        this.auditItem = auditItem;
        this.auditClass = auditClass;
        this.idAuditItem = idAuditItem;
    }

    @Generated(hash = 1460163894)
    public AuditItem() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuditItem() {
        return this.auditItem;
    }

    public void setAuditItem(String auditItem) {
        this.auditItem = auditItem;
    }

    public String getAuditClass() {
        return this.auditClass;
    }

    public void setAuditClass(String auditClass) {
        this.auditClass = auditClass;
    }

    public Long getIdAuditItem() {
        return this.idAuditItem;
    }

    public void setIdAuditItem(Long idAuditItem) {
        this.idAuditItem = idAuditItem;
    }


}
