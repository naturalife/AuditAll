package com.faraway.auditall.Utils;

import android.util.Log;

import com.faraway.auditall.DataBase.AuditInfo;
import com.faraway.auditall.DataBase.AuditItem;
import com.faraway.auditall.DataBase.AuditPhotos;
import com.faraway.auditall.DataBase.BasicInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author ${Faraway}
 * @description
 * @date ${data}
 **/
public class ExcelUtil {
    private static WritableFont arial14font = null;
    private static WritableCellFormat arial14format = null;

    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;

    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;

    private static WritableFont arial11font = null;
    private static WritableCellFormat arial11format = null;

    private final static String UTF8_ENCODING = "UTF-8";

    /**
     * 单元格的格式设置：字体大小、颜色、对齐方式、背景颜色等……
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(Colour.LIGHT_BLUE);

            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(Alignment.CENTRE);
            arial14format.setVerticalAlignment(VerticalAlignment.CENTRE);
            arial14format.setBorder(Border.ALL, BorderLineStyle.NONE);
//            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);
            arial14format.setWrap(false);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            arial10font.setColour(Colour.BLACK);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(Alignment.CENTRE);
            arial10format.setVerticalAlignment(VerticalAlignment.CENTRE);
            arial10format.setBorder(Border.ALL, BorderLineStyle.NONE);
            arial10format.setBackground(Colour.GREY_25_PERCENT);

            arial12font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            arial12font.setColour(Colour.BLACK);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setWrap(true);
            //对齐格式
            arial12format.setAlignment(Alignment.CENTRE);
            arial12format.setVerticalAlignment(VerticalAlignment.CENTRE);
            //设置边框
            arial12format.setBorder(Border.ALL, BorderLineStyle.THIN);


            arial11font = new WritableFont(WritableFont.ARIAL, 10);
            arial11format = new WritableCellFormat(arial11font);
//            arial11format.setWrap(true);
            //对齐格式
            arial11format.setAlignment(Alignment.CENTRE);
            arial11format.setVerticalAlignment(VerticalAlignment.CENTRE);
            //设置边框
            arial11format.setBorder(Border.ALL, BorderLineStyle.NONE);


        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Excel
     *
     * @param filePath  存放Excel文件的路径（path/demo.xls）
     * @param sheetName Excel表名
     * @param colName   Excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, String sheetName, String[] colName, String[] basicInfoItem, List<BasicInfo> basicInfoList) {
        format();

        WritableWorkbook workbook = null;
        BasicInfo basicInfo = new BasicInfo();
        if (basicInfoList.size() > 0) {
            basicInfo.setAuditContent(basicInfoList.get(0).getAuditContent());//获得标题：审核内容

            basicInfo.setAuditor(basicInfoList.get(0).getAuditor());//获得审核人
            basicInfo.setTime(basicInfoList.get(0).getTime());//获得审核时间
            basicInfo.setAuditTarget(basicInfoList.get(0).getAuditTarget());//获得班组
            basicInfo.setArea(basicInfoList.get(0).getArea());//获得层级

            basicInfo.setTotalYesNumber(basicInfoList.get(1).getTotalYesNumber());
            basicInfo.setTotalNoNumber(basicInfoList.get(1).getTotalNoNumber());

        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else if (file.exists()) {
                workbook = Workbook.createWorkbook(file);
            } else {
                return;
            }

            //根据文件路径和名称、创建excle表
            workbook = Workbook.createWorkbook(file);

            //设置EXCEL表格的名字
            WritableSheet sheet = workbook.createSheet(sheetName, 0);

            //插入表头等信息
            sheet.addCell(new Label(2, 1, basicInfo.getAuditContent() + "报告", arial14format));
            sheet.addCell(new Label(1, 3, basicInfo.getAuditor(), arial11format));//审核人
            sheet.addCell(new Label(3, 3, basicInfo.getTime() + " ", arial11format));//时间

            if ((basicInfo.getTotalYesNumber()) > 0) {
                sheet.addCell(new Label(5, 3, (int) (((float) (basicInfo.getTotalYesNumber()) / (basicInfo.getTotalYesNumber() + basicInfo.getTotalNoNumber())) * 100) + "%", arial11format));

            }//符合情况
            sheet.addCell(new Label(1, 4, basicInfo.getAuditTarget(), arial11format));//班组
            sheet.addCell(new Label(3, 4, basicInfo.getArea(), arial11format));//层级


            //审核信息的说明：
            sheet.addCell(new Label(0, 3, basicInfoItem[0], arial10format));//审核人
            sheet.addCell(new Label(2, 3, basicInfoItem[1], arial10format));//审核时间
            sheet.addCell(new Label(4, 3, basicInfoItem[2], arial10format));//符合情况
            sheet.addCell(new Label(0, 4, basicInfoItem[3], arial10format));//审核对象
            sheet.addCell(new Label(2, 4, basicInfoItem[4], arial10format));//审核层级


            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 6, colName[col], arial10format));//标题栏
            }

            //设置行高
            sheet.setRowView(1, 500);
            sheet.setRowView(3, 350);
            sheet.setRowView(4, 350);
            sheet.setRowView(6, 350);

            //设置列宽
            sheet.setColumnView(3, 26);
            sheet.setColumnView(4, 26);
            sheet.setColumnView(6, 26);

            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //audit图片写入excel
    @SuppressWarnings("unchecked")
    public static void writePictureToExcel(List<AuditPhotos> photoList, String filePath, int auditId, String fileName, int startColum) {
        if (photoList != null && photoList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            startColum = 5;

            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));//打开输出流

                Workbook workbook = Workbook.getWorkbook(in);//获得输出流
                writebook = Workbook.createWorkbook(new File(fileName), workbook);//打开文件

                WritableSheet sheet = writebook.getSheet(0);//获得第一个工作簿

                for (int i = 0; i < photoList.size(); i++) {

                    //转换后PNG图片存放地址
                    String filePathPic = filePath + "/auditPic" + auditId + i + ".png";

                    //新建图片文件
                    File filePicture = new File(filePathPic);

                    //图片文件转PNG（存于filePicture）
                    ImageUtils.saveBitmapAsPng(photoList.get(i).getPhotoPath(), filePicture);

                    Log.d("AuditFragment", "EXCELUTI" + "-->" + auditId+"--"+photoList.get(i).getPhotoPath());

                    //新建图片文件list（fileList）,插入excel用
                    List<File> fileList = new ArrayList<>();

                    // 转换后PNG文件存入fileList
                    fileList.add(filePicture);

                    //图片插入到单元格
                    WritableImage image = new WritableImage(startColum + i - 1, 7 + auditId, 0.95, 0.95, filePicture);

                    sheet.setColumnView(startColum + i - 1, 16);//设置列宽

                    sheet.addCell(new Label(startColum + i - 1, 6, "图片" + "-" + (i + 1), arial10format));//更新excel表头

                    // 把图片插入到sheet
                    sheet.addImage(image);

                    // 清空filePathPic
                    filePathPic = "";

                    Log.i("AuditFragment","P"+"-"+auditId+"-"+"filePicture:"+filePicture+"写入成功");
                }
                writebook.write();//写入excel

                workbook.close();

            } catch (Exception e) {
                Log.i("AuditFragment","P"+"-"+auditId+"-"+"filePicture:"+"写入失败");
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 将所有Audit信息写入Excel中
     *
     * @param auditInfoList 单个审核list
     * @param fileName      excel文件名（路径+***.xls）
     */

    @SuppressWarnings("unchecked")
    public static void writeAllAuditToExcel(List<AuditInfo> auditInfoList, List<AuditItem> auditItemList, String fileName) {
        if (auditInfoList != null && auditInfoList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            Workbook workbook;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));//打开输入流
                workbook = Workbook.getWorkbook(in);//获得Excel
                writebook = Workbook.createWorkbook(new File(fileName), workbook);//打开文件
                WritableSheet sheet = writebook.getSheet(0);//获得第一个工作簿

                for (int j = 0; j < auditInfoList.size(); j++) {//

                    AuditInfo mAudit = (AuditInfo) auditInfoList.get(j);//获得数据
                    AuditItem auditItem = (AuditItem) auditItemList.get(j);
                    List<String> list = new ArrayList<>();//添加数据至list
                    list.add(String.valueOf(mAudit.getId() + 1));//list[0]第一列数据
                    list.add(auditItem.getAuditItem());//list[1]第二列数据


                    if (null != String.valueOf(mAudit.getAuditResult())) {
                        list.add(String.valueOf(mAudit.getAuditResult()));
                    } else {
                        list.add("-");
                    }//list[2]第三列数据

                    if (null != mAudit.getAuditFind()) {
                        list.add(mAudit.getAuditFind());
                    } else {
                        list.add("-");
                    }//list[3]第四列数据

                    for (int i = 0; i < list.size(); i++) {
//                        Cell cell = sheet.getCell(i, j + 1);//获取单元格
//                        if (cell.getType() != CellType.EMPTY) {
                        sheet.addCell(new Label(i, j + 7, list.get(i), arial12format));//数据添加至表格
                        if (list.get(i) != null) {
                            if (length(list.get(i)) <= 3) {
                                //设置列宽
                                sheet.setColumnView(i, list.get(i).length() + 8);
                            } else {
                                sheet.setColumnView(i, list.get(i).length() + 12);
                            }
                        }
                        //设置行高
                        sheet.setRowView(j + 7, 1200);

                    }//单元格判空
                }
                writebook.write();//写入excel
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 将单个Audit信息写入Excel中
     *
     * @param basicInfoList 审核基本信息list
     * @param auditInfoList 单个审核list
     * @param photoList     审核图片list
     * @param filePath      文件夹路径
     * @param fileName      excel文件名（路径+***.xls）
     */

    @SuppressWarnings("unchecked")
    public static void writeSingleAuditToExcel(List<BasicInfo> basicInfoList, List<AuditInfo> auditInfoList, List<AuditPhotos> photoList, int auditId, String filePath, String fileName) {
        if (auditInfoList != null && auditInfoList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));//打开输出流

                Workbook workbook = Workbook.getWorkbook(in);//获得输出流
                writebook = Workbook.createWorkbook(new File(fileName), workbook);//打开文件

                WritableSheet sheet = writebook.getSheet(0);//获得第一列第一行单元格

                //audit信息写入excel

                //图片开始插入的列号：startColum-1
                int startColum = auditInfoList.size();

                writebook.write();//写入excel
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    //audit信息写入excel
    @SuppressWarnings("unchecked")
    public static void wirteAuditInfoToExcel(List<AuditInfo> auditInfoList, String auditItem, int auditId, String fileName) {
        if (auditInfoList != null && auditInfoList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;

            AuditInfo mAudit = (AuditInfo) auditInfoList.get(auditId);//获得数据
            List<String> list = new ArrayList<>();//添加数据至list
            list.add(String.valueOf(mAudit.getId() + 1));//list[0]第一列数据
            list.add(auditItem);//list[1]第二列数据
            if (null != String.valueOf(mAudit.getAuditResult())) {
                list.add(String.valueOf(mAudit.getAuditResult()));
            } else {
                list.add("-");
            }//list[2]第三列数据

            if (null != mAudit.getAuditFind()) {
                list.add(mAudit.getAuditFind());
            } else {
                list.add("-");
            }//list[3]第四列数据

            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));//打开输出流

                Workbook workbook = Workbook.getWorkbook(in);//获得输出流
                writebook = Workbook.createWorkbook(new File(fileName), workbook);//打开文件

                WritableSheet sheet = writebook.getSheet(0);//获得第一列第一行单元格


                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(i, auditId + 1, list.get(i), arial12format));//数据添加至表格

                    if (length(list.get(i)) <= 4) {
                        //设置列宽
                        sheet.setColumnView(i, length(list.get(i)) + 8);
                    } else {
                        sheet.setColumnView(i, length(list.get(i)) + 6);
                    }
                    //设置行高
                    sheet.setRowView(1 + auditId, 1200);
                }
                writebook.write();//写入excel
                workbook.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}