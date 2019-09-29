package com.faraway.auditall.SelectPhoto;

import java.io.Serializable;

/**
 * @author Fan
 * @description 图片Bean
 * @date 2019-08-19
 * @time 13:39
 **/
public class PictureBean implements Serializable {
    private String photoPath;//图片路径
    private int photoNumber;//图片编号
    private boolean selectStatue = false;//图片被选择状态

    public boolean isSelectStatue() {
        return selectStatue;
    }

    public void setSelectStatue(boolean selectStatue) {
        this.selectStatue = selectStatue;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(int photoNumber) {
        this.photoNumber = photoNumber;
    }

}
