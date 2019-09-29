package com.faraway.auditall.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Fan
 * @description图片处理类
 * @date 2019-09-16
 * @time 9:54
 **/
public class ImageUtils {

    /**
     * 将任何格式的图片转成PNG
     *
     * @param photoPath 待转图片的存放路径
     * @param file 转换后的文件
     *
     */

    public static void saveBitmapAsPng(String photoPath, File file) {
        try {
            BitmapFactory.Options options = new  BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap bitmap = BitmapFactory.decodeFile(photoPath,options);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 将任何格式的图片转成PNG
     *
     * @param photoPath 图片的存放路径
     * @param rowNum 图片所占的行数
     *
     */

    public static double getPhotoColNums(String photoPath,double rowNum){

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        //根据图片尺寸，计算行数与列数
        int height = bitmap.getHeight();//获得图片宽度
        int width = bitmap.getWidth();//获得图片高度
        long rationPicture = height > width ? height/width : width/height;//计算图片行高比

        double rowPix = rowNum * 12.75 / 0.75;//计算行的像素值12.75为行高，0.75为行高与像素比
        double colPix = rowPix / rationPicture;//根据行高和宽高比，计算列像素值
        double colNum =(int) rowPix * 0.131719 / 8.43;//根据列像素值，计算列数；0.131719位列宽与像素比，8.43位列宽

        return colNum;//返回所占的列数
    }





}
