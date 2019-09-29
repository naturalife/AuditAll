package com.faraway.auditall.DataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Fan
 * @description
 * @date 2019-09-24
 * @time 9:31
 **/
@Entity
public class AuditPhotos {
    @Id(autoincrement = true)
    private Long id;

    private long position;//recyclerView中位置

    private long idAuditPhotos;//外键

    private String photoPath;//图片路径

    @Generated(hash = 906992285)
    public AuditPhotos(Long id, long position, long idAuditPhotos,
            String photoPath) {
        this.id = id;
        this.position = position;
        this.idAuditPhotos = idAuditPhotos;
        this.photoPath = photoPath;
    }

    @Generated(hash = 1049959081)
    public AuditPhotos() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPosition() {
        return this.position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getIdAuditPhotos() {
        return this.idAuditPhotos;
    }

    public void setIdAuditPhotos(long idAuditPhotos) {
        this.idAuditPhotos = idAuditPhotos;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }



}
