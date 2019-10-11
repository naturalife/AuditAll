package com.faraway.auditall.DataBase;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fan
 * @description数据库单例
 * @date 2019-09-02
 * @time 14:04
 **/
public class App extends Application {
    private DaoSession daoSession;
    private  List<String> auditItemList;
    private  List<String> cp1;//冲压通用
    private  List<String> hp1;//焊接通用
    private  List<String> bp1;//钣金通用
    private  List<String> jp1;//钣金通用
    private  List<String> wp1;//钣金通用
    private  List<String> zp1;//质量通用
    private  List<String> cp2;//工段
    private  List<String> cp3;//科室
    private  List<String> cp4;//部门
    private  List<String> cp5;//公司
    private Map<String,List<String>> auditItemMap;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();
    }

    private void initData() {
        auditItemList = new ArrayList<>();

        zp1 = new ArrayList<>(Arrays.asList(
                "员工是否遵守安全规定、正确佩戴劳保用品",
                "车间现场是否存在未及时整改的安全隐患",
                "工作现场管理、可视化管理标准，是否有效执行",
                "车间物品、检具、不合格产品及原料、量具是否规范使用并按照定置摆放",
                "现场日常点检、维护保养是否有效完成，并正确记录",
                "返工/报废单描述的数量及问题是否与实际数量及问题状态一致",
                "检验操作过程和方法是否符合标准作业文件要求",
                "现场工艺文件是否齐全、清晰、完整、有效，是否按规定执行",
                "首中未件及过程巡查,是否有效实施",
                "确保检验记录和检测量具,是否完整",
                "产品特殊的检验要求，员工是否按照要求执行",
                "不合格品是否有效隔离，并标识记录",
                "员工是否了解每种产品质量状态，判定是否准确",
                "员工是否对质量标准产品控制计划完全掌握并熟练运用",
                "是否按照客户要求，实施纠正预防措施",
                "现场是否存在浪费现象以及改进机会",
                "员工是否遵守劳动、工艺纪律",
                "员工的培训，是否有效",
                "设备工装是否完好，有无跑冒滴漏现象"));

        wp1 = new ArrayList<>(Arrays.asList(
                "员工是否遵守安全规定、正确佩戴劳保用品",
                "消防通道、安全出口是否被占用",
                "料箱是否没有放到位，是否有倒塌风险",
                "叉车及物流车辆是否超速行驶",
                "工作现场是否安全，整洁，有序（原材料区&成品库区&辅料库区）",
                "操作者是否胜任该工作（如：岗位技能矩阵表等）",
                "现场日常点检、维护保养是否有效完成，并正确记录",
                "叉车及物流车辆是否停在指定区域内",
                "现场作业指导书是否齐全、清晰、完整、有效",
                "作业是否参照作业指导书进行操作",
                "叉车是否按规定线路行驶",
                "所有物资是否按区划分进行存储",
                "物资标识是否清晰、明确",
                "是否设置看板表明库存状态，账卡物一致",
                "是否进行有效的先进先出管理",
                "是否能够确保不同批次产品得到区分，不混批",
                "是否对入库制件进行了有效防护",
                "是否按照客户要求，实施纠正预防措施",
                "现场是否存在浪费现象以及改进机会",
                "员工是否遵守劳动、工艺纪律",
                "员工的培训，是否有效",
                "设备工装是否完好，有无跑冒滴漏现象"));
        jp1 = new ArrayList<>(Arrays.asList(
                "员工是否遵守安全规定、正确佩戴劳保用品",
                "车间现场是否存在未及时整改的安全隐患",
                "工作现场管理、可视化管理标准，是否有效执行",
                "车间物品、器具、原料、辅具，是否规范使用并按定置摆放",
                "现场日常点检、维护保养是否有效完成，并正确记录",
                "实际生产的工艺参数，是否符合工艺文件规定",
                "生产操作过程和方法，是否符合标准作业文件要求",
                "现场工艺文件，是否齐全、清晰、完整、有效",
                "首末件及过程检验，是否有效实施",
                "原材料规格尺寸是否与工艺文件标准一致",
                "产品的特殊的操作技巧，员工是否按要求执行",
                "可疑品、不合格品，是否有效隔离并标识",
                "是否按照客户要求，实施纠正预防措施",
                "现场是否存在浪费现象以及改进机会",
                "员工是否遵守劳动、工艺纪律",
                "员工的培训，是否有效",
                "设备工装是否完好，有无跑冒滴漏现象"));
        bp1 = new ArrayList<>(Arrays.asList(
                "员工是否遵守安全规定、正确佩戴劳保用品",
                "车间现场是否存在未及时整改的安全隐患",
                "工作现场管理、可视化管理标准，是否有效执行",
                "车间物品、器具、原料、辅具，是否规范使用并按定置摆放",
                "现场日常点检、维护保养是否有效完成，并正确记录",
                "返修作业焊机参数使用是否按照标准执行",
                "返修操作过程和方法是否符合标准作业文件要求",
                "返修首件是否经过质检确认",
                "现场工艺文件是否齐全、清晰、完整、有效",
                "现场返修制件装箱卡、返工单、制件返工缺陷,是否一致",
                "防错装置功能，是否正常并被验证",
                "员工是否理解各工序的关键特性，高风险项目、特殊工艺是否受控",
                "是否按照客户要求，实施纠正预防措施",
                "现场是否存在浪费现象以及改进机会",
                "员工是否遵守劳动、工艺纪律",
                "员工的培训，是否有效",
                "设备工装是否完好，有无跑冒滴漏现象"));
        cp1 = new ArrayList<>(Arrays.asList(
                "员工是否遵守安全规定、正确佩戴劳保用品",
                "车间现场是否存在未及时整改的安全隐患",
                "工作现场管理、可视化管理标准，是否有效执行",
                "车间物品、器具、原料、辅具，是否规范使用并按定置摆放",
                "现场日常点检、维护保养是否有效完成，并正确记录",
                "实际生产的工艺参数，是否符合工艺文件规定",
                "生产操作过程和方法，是否符合标准作业文件要求",
                "现场工艺文件，是否齐全、清晰、完整、有效",
                "首末件及过程检验，是否有效实施",
                "检验记录和过程操作记录，是否完成",
                "产品的特殊的操作技巧，员工是否按要求执行",
                "防错装置功能，是否正常并被验证",
                "可疑品、不合格品，是否有效隔离并标识",
                "员工是否理解各工序的关键特性，高风险项目、特殊工艺是否受控",
                "是否按照客户要求，实施纠正预防措施",
                "现场是否存在浪费现象以及改进机会",
                "员工是否遵守劳动、工艺纪律",
                "员工的培训，是否有效",
                "设备工装是否完好，有无跑冒滴漏现象"));
        hp1 = new ArrayList<>(Arrays.asList(
                "员工是否遵守安全规定、正确佩戴劳保用品",
                "车间现场是否存在未及时整改的安全隐患",
                "工作现场管理、可视化管理标准，是否有效执行",
                "凸焊物品、器具、原料、辅具是否规范使用并按照定置摆放",
                "现场日常点检、维护保养是否有效完成，并正确记录",
                "实际生产的工艺参数，是否符合工艺文件规定",
                "生产操作过程和方法，是否符合标准作业文件要求",
                "现场工艺文件，是否齐全、清晰、完整、有效",
                "现场螺母是否按照计划配送上线",
                "首末件及过程检验，是否有效实施",
                "检验记录和过程操作记录，是否完成",
                "产品的特殊的操作技巧，员工是否按要求执行",
                "防错装置功能，是否正常并被验证",
                "可疑品、不合格品，是否有效隔离并标识",
                "员工是否理解各工序的关键特性，高风险项目、特殊工艺是否受控",
                "是否按照客户要求，实施纠正预防措施",
                "现场是否存在浪费现象以及改进机会",
                "员工是否遵守劳动、工艺纪律",
                "员工的培训，是否有效",
                "设备工装是否完好，有无跑冒滴漏现象"));

        cp2 = new ArrayList<>(Arrays.asList(
                "管理范围内，人员是否按规定的频次实施分层审核",
                "生产出现异常时，是否实施纠正预防措施",
                "是否符合过程控制计划"));

        cp3 = new ArrayList<>(Arrays.asList(
                "过程审核的问题点，是否按节点整改完成",
                "分层审核的不符合项目及整改情况，是否展示",
                "顾客反馈的问题，是否按时间节点整改"));

        cp4 = new ArrayList<>(Arrays.asList(
                "管理看板中的内容，是否定期更新",
                "各科室月、周工作计划，是否在按时开展"));

        cp5 = new ArrayList<>(Arrays.asList(
                "部门月度的工作计划是否按节点完成",
                "公司成本及改善活动是否有续的在开展"));

        auditItemMap = new HashMap<>();//返回的Map
        //冲压审核表
        auditItemMap.put("0",cp1);
        auditItemMap.put("6",mergeLists(cp1,cp2));
        auditItemMap.put("12",mergeLists(cp1,cp2,cp3));
        auditItemMap.put("18",mergeLists(cp1,cp2,cp3,cp4));
        auditItemMap.put("24",mergeLists(cp1,cp2,cp3,cp4,cp5));

        //焊接审核表
        auditItemMap.put("1",mergeLists(hp1));
        auditItemMap.put("7",mergeLists(hp1,cp2));
        auditItemMap.put("13",mergeLists(hp1,cp2,cp3));
        auditItemMap.put("19",mergeLists(hp1,cp2,cp3,cp4));
        auditItemMap.put("25",mergeLists(hp1,cp2,cp3,cp4,cp5));

        //剪板审核表
        auditItemMap.put("2",mergeLists(jp1));
        auditItemMap.put("8",mergeLists(jp1,cp2));
        auditItemMap.put("14",mergeLists(jp1,cp2,cp3));
        auditItemMap.put("20",mergeLists(jp1,cp2,cp3,cp4));
        auditItemMap.put("26",mergeLists(jp1,cp2,cp3,cp4,cp5));

        //钣金审核表
        auditItemMap.put("3",mergeLists(bp1));
        auditItemMap.put("9",mergeLists(bp1,cp2));
        auditItemMap.put("15",mergeLists(bp1,cp2,cp3));
        auditItemMap.put("21",mergeLists(bp1,cp2,cp3,cp4));
        auditItemMap.put("27",mergeLists(bp1,cp2,cp3,cp4,cp5));

        //质量审核表
        auditItemMap.put("4",mergeLists(zp1));
        auditItemMap.put("10",mergeLists(zp1,cp2));
        auditItemMap.put("16",mergeLists(zp1,cp2,cp3));
        auditItemMap.put("22",mergeLists(zp1,cp2,cp3,cp4));
        auditItemMap.put("28",mergeLists(zp1,cp2,cp3,cp4,cp5));

        //物流审核表
        auditItemMap.put("5",mergeLists(wp1));
        auditItemMap.put("11",mergeLists(wp1,cp2));
        auditItemMap.put("17",mergeLists(wp1,cp2,cp3));
        auditItemMap.put("23",mergeLists(wp1,cp2,cp3,cp4));
        auditItemMap.put("29",mergeLists(wp1,cp2,cp3,cp4,cp5));

    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    public Map<String,List<String>> getAuditItemMap(){
        return auditItemMap;
    }


    //合并多个list
    public static <T> List<T> mergeLists(List<T>... lists) {
        Class clazz = lists[0].getClass();
        List<T> list = null;
        try {
            list = (List<T>) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0, len = lists.length; i < len; i++) {
            list.addAll(lists[i]);
        }
        return list;
    }

}
