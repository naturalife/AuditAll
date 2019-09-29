package com.faraway.auditall;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.faraway.auditall.DataBase.App;
import com.faraway.auditall.DataBase.AuditInfo;
import com.faraway.auditall.DataBase.AuditInfoDao;
import com.faraway.auditall.DataBase.AuditItem;
import com.faraway.auditall.DataBase.AuditItemDao;
import com.faraway.auditall.DataBase.AuditPhotos;
import com.faraway.auditall.DataBase.AuditPhotosDao;
import com.faraway.auditall.DataBase.BasicInfo;
import com.faraway.auditall.DataBase.BasicInfoDao;
import com.faraway.auditall.DataBase.DaoSession;
import com.faraway.auditall.Utils.ExcelUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 初始化数据库，跳转至AuditPagerActivity
 */

public class LoginActivity extends AppCompatActivity {

    private Button button_login;
    private EditText editText_userName;
    private EditText editText_password;
    private EditText editText_auditAim;
    private Spinner spinner_class;
    private Spinner spinner_content;

    private BasicInfoDao basicInfoDao;
    private AuditInfoDao auditInfoDao;
    private AuditItemDao auditItemDao;
    private AuditPhotosDao auditPhotosDao;

    private BasicInfo basicInfo;
    private AuditInfo auditInfo;
    private AuditPhotos auditPhotos;

    private String userName;//审核人
    private String password;//登录密码
    private String className;//审核级别
    private String auditContent;//审核项目
    private String auditTarget;//审核对象
    private String auditClass;//审核对象
    private String auditDate;//审核日期
    private String realPassword = "111";
    private String filePath = Environment.getExternalStorageDirectory() + "/AuditSomething";//excel和图片存放路径
    private String fileNameExcel = "/AuditAll.xls";//excel文件名字
    private String filePathExcel;//excel文件路径（filePaht+fileNameExcel），新建excel文件用


    private int auditAreaNum;
    private int auditClassNum;
    private int auditContentNum;
    private int idAuditItem;

    private List<AuditInfo> auditInfoList = new ArrayList<>();
    private List<BasicInfo> basicInfoList = new ArrayList<>();
    private List<String> spinnerContentList = new ArrayList<String>();//审核内容列表,后期改成后台可修改
    private List<String> spinnerClassList = new ArrayList<String>();//审核级别列表,后期改成后台可修改
    private List<AuditItem> auditItemList = new ArrayList<AuditItem>();//审核项目列表,后期改成后台可修改
    private List<String> spinnerAreaList = new ArrayList<String>();//审核项目列表,后期改成后台可修改


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialDao();//初始化数据库

        initData();//初始化各类按钮等

        initSpinner_class();//初始化审核级别下拉菜单

        initSpinner_content();//初始化审核项目下拉菜单

        getAuditDate();//获得当前日期


        editText_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userName = charSequence.toString();
                button_login.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_auditAim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                auditTarget = charSequence.toString();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean a = (
                        ((userName != null) || (!"".equals(userName))) &&
                                ((auditTarget != null) || (!"".equals(auditTarget))) &&
                                ((auditClass != null) || (!"".equals(auditClass))) &&
                                ((auditContent != null) || (!"".equals(auditContent))) &&
                                ((auditContent != null) && (realPassword.equals(password))));


                boolean b = (((userName == null) || ("".equals(userName))) ||
                        ((auditTarget == null) || ("".equals(auditTarget))) ||
                        ((auditClass == null) || ("".equals(auditClass))) ||
                        ((auditContent == null) || ("".equals(auditContent))));

                if (("faraway".equals(userName)) && ("222".equals(password))) {
                    Intent intent = new Intent(LoginActivity.this, SuperEditActivity.class);
                    startActivity(intent);
                    finish();
                } else if (a) {

                    basicInfo.setAuditor(userName);
                    basicInfo.setArea(auditClass);
                    basicInfo.setAuditContent(auditContent);
                    basicInfo.setAuditTarget(auditTarget);
                    basicInfo.setAuditItemNum(idAuditItem);

                    initBasicInfoDao();
                    getIdAuditItem();//获得审核项目id

//                    Log.d("HHH", "LoginActivity" + "--->" + "1" + "--->" + filePathExcel);

                    Intent intent = new Intent(LoginActivity.this, AuditPagerActivity.class);
                    startActivity(intent);
                    finish();
                } else if (b) {
                    Toast.makeText(LoginActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();

                } else if (!(realPassword.equals(password))) {
                    Toast.makeText(LoginActivity.this, "密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void getAuditDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        auditDate = year + "-" + month + "-" + day;
    }


    private void initData() {
        button_login = findViewById(R.id.button_loginActivity_login);
        editText_userName = findViewById(R.id.editText_loginActivity_userName);
        editText_password = findViewById(R.id.editText_loginActivity_password);
        editText_auditAim = findViewById(R.id.editText_loginActivity_auditAim);
        spinner_class = findViewById(R.id.spinner_loginActivity_class);
        spinner_content = findViewById(R.id.spinner_loginActivity_content);


        button_login.setEnabled(false);
        editText_userName.setHintTextColor(getResources().getColor(R.color.divider));
        editText_password.setHintTextColor(getResources().getColor(R.color.divider));
        editText_auditAim.setHintTextColor(getResources().getColor(R.color.divider));

        spinnerAreaList.add("冲压");
        spinnerAreaList.add("焊接");
        spinnerAreaList.add("剪板");
        spinnerAreaList.add("工装");
        spinnerAreaList.add("设备");
        spinnerAreaList.add("物流");
        spinnerAreaList.add("请选择:审核范围");

    }

    private void getIdAuditItem() {
        if (auditTarget != null) {
            if ((auditTarget.contains("冲")) || (auditTarget.contains("D")) || (auditTarget.contains("d"))) {
                auditTarget = "冲压";
                auditAreaNum = 0;
            } else if ((auditTarget.contains("焊")) || (auditTarget.contains("W"))|| (auditTarget.contains("w"))) {
                auditTarget = "焊接";
                auditAreaNum = 1;
            } else if ((auditTarget.contains("剪"))||(auditTarget.contains("C"))||(auditTarget.contains("c"))) {
                auditTarget = "剪板";
                auditAreaNum = 2;
            } else if ((auditTarget.contains("工装"))||(auditTarget.contains("M"))||(auditTarget.contains("m"))) {
                auditTarget = "工装";
                auditAreaNum = 3;
            } else if ((auditTarget.contains("设备"))||(auditTarget.contains("E"))||(auditTarget.contains("e"))) {
                auditTarget = "设备";
                auditAreaNum = 4;
            } else if ((auditTarget.contains("物流"))||(auditTarget.contains("L"))||(auditTarget.contains("l"))) {
                auditTarget = "工装";
                auditAreaNum = 5;
            }
        }

        for (int i = 0; i < spinnerContentList.size() - 1; i++) {
            if ((auditContent!=null) &&(auditContent.equals(spinnerContentList.get(i)))) {
                auditContentNum = i;
            }
        }
        for (int i = 0; i < spinnerClassList.size() - 1; i++) {
            if ((auditClass != null)&&(auditClass.equals(spinnerClassList.get(i)))) {
                auditClassNum = i;
            }
        }

        if (auditContentNum == 0) {
            idAuditItem = (auditContentNum + 4) * 6 + auditClassNum * 6 + auditAreaNum - 24;
        } else {
            idAuditItem = (auditContentNum + 4) * 6 + auditAreaNum;
        }
    }

    private void initSpinner_class() {

        spinnerClassList.add("班组级");
        spinnerClassList.add("工段级");
        spinnerClassList.add("科室级");
        spinnerClassList.add("部门级");
        spinnerClassList.add("公司级");
        spinnerClassList.add("请选择:审核范围");

        final MyAdapterLoginActivity arrAdapter2 = new MyAdapterLoginActivity(this, spinnerClassList);
        spinner_class.setAdapter(arrAdapter2);
        spinner_class.setSelection(spinnerClassList.size() - 1, true);

        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                editText_auditAim.clearFocus();
//                editText_auditAim.setCursorVisible(false);
                auditClass = arrAdapter2.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //判断字符串是否相等
    private boolean ifStringEquals(String s, String target) {
        boolean res = false;
        if ((s != null) && (s.equals(target))) {
            res = true;
        }
        return res;
    }


    private void initSpinner_content() {
        spinnerContentList.add("分层审核");
        spinnerContentList.add("安全审核");
        spinnerContentList.add("抽样检验");
        spinnerContentList.add("班组日志");
        spinnerContentList.add("请选择:审核项目");

        final MyAdapterLoginActivity arrAdapter = new MyAdapterLoginActivity(this, spinnerContentList);
        spinner_content.setAdapter(arrAdapter);
        spinner_content.setSelection(spinnerContentList.size() - 1, true);

        spinner_content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                auditContent = arrAdapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //初始化数据库
    private void initialDao() {
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        daoSession.clear();
        auditInfoDao = daoSession.getAuditInfoDao();
        auditPhotosDao = daoSession.getAuditPhotosDao();
        basicInfoDao = daoSession.getBasicInfoDao();
        auditItemDao = daoSession.getAuditItemDao();

        auditInfo = new AuditInfo();
        basicInfo = new BasicInfo();
    }


    //获得数据库数据至auditInfoList
    private void getAuditInfoList() {
        if (auditInfoDao != null) {
            auditInfoList = auditInfoDao.queryBuilder().list();
        }
    }

    //获得数据库数据至basicInfoList
    private void getBasicInfoList() {
        if (basicInfoDao != null) {
            basicInfoList = basicInfoDao.queryBuilder().list();
        }
    }


    //更新basicInfoList
    private void initBasicInfoDao() {
        basicInfo.setId(1L);
        basicInfo.setTime(auditDate);
        basicInfoDao.insert(basicInfo);
    }


    //获得数据库数据，并放入auditItemList中
    private void getAuditItemList() {
        if (auditItemDao != null) {
            auditItemList = auditItemDao.queryBuilder()
                    .where(AuditItemDao.Properties.IdAuditItem.eq(idAuditItem))
                    .list();
        }
    }

}
