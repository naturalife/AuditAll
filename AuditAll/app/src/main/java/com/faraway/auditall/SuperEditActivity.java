package com.faraway.auditall;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.faraway.auditall.DataBase.App;
import com.faraway.auditall.DataBase.AuditItem;
import com.faraway.auditall.DataBase.AuditItemDao;
import com.faraway.auditall.DataBase.DaoSession;

import java.util.ArrayList;
import java.util.List;

public class SuperEditActivity extends AppCompatActivity {

    private AuditItemDao auditItemDao;
    private AuditItem auditItem;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;

    private Spinner spinnerArea;
    private Spinner spinnerClass;
    private Spinner spinnerContent;

    private Button buttonFinish;
    private Button buttonQuit;


    private List<String> spinnerAreaList;
    private List<String> spinnerClassList;
    private List<String> spinnerContentList;
    private List<String> auditItemList;
    private List<AuditItem> auditItemListBefore;


    private String auditArea;
    private String auditClass;
    private String auditContent;

    private int auditAreaNum;
    private int auditClassNum;
    private int auditContentNum;
    private int idAuditItem;


    private String stringAuditItem1;
    private String stringAuditItem2;
    private String stringAuditItem3;
    private String stringAuditItem4;
    private String stringAuditItem5;
    private String stringAuditItem6;

    private int j = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_edit);

        initData();

        initialDao();

        getAuditItemListBefore();

        buttonQuit.setEnabled(false);
        initSpinnerArea();
        initSpinnerClass();
        initSpinnerContent();

        addAuditItemList1();
        addAuditItemList2();
        addAuditItemList3();
        addAuditItemList4();
        addAuditItemList5();
        addAuditItemList6();



        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j++;

                boolean a = ifStringNull(stringAuditItem1) && ifStringNull(auditArea)
                        && ifStringNull(auditClass) && ifStringNull(auditContent);

                if (a) {
                    if (j < 8) {//共8页
                        textView1.setText(" " + (1 + j * 6));
                        textView2.setText(" " + (j * 6 + 2));
                        textView3.setText(" " + (j * 6 + 3));
                        textView4.setText(" " + (j * 6 + 4));
                        textView5.setText(" " + (j * 6 + 5));
                        textView6.setText(" " + (j * 6 + 6));


                        addItemList(stringAuditItem1);
                        addItemList(stringAuditItem2);
                        addItemList(stringAuditItem3);
                        addItemList(stringAuditItem4);
                        addItemList(stringAuditItem5);
                        addItemList(stringAuditItem6);


                        editText1.setText(null);
                        editText2.setText(null);
                        editText3.setText(null);
                        editText4.setText(null);
                        editText5.setText(null);
                        editText6.setText(null);

                        buttonQuit.setEnabled(true);

                    } else {
                        Toast.makeText(SuperEditActivity.this, "已达到系统上限，无法继续添加", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SuperEditActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }

            }
        });


        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getIdAuditItem();


                for (AuditItem auditItem : auditItemListBefore){
                    if (auditItem.getIdAuditItem()==idAuditItem){
                        auditItemDao.delete(auditItem);
                    }
                }


                addAuditItemDao(0L + idAuditItem);

//                Intent intent = new Intent(SuperEditActivity.this,InitialPageActivity.class);
//                intent.putExtra("TAG_EXIT", true);
//                startActivity(intent);

            }
        });

    }

    private void getIdAuditItem() {
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
        for (int i = 0; i < spinnerAreaList.size() - 1; i++) {
            if ((auditArea!=null)&&(auditArea.equals(spinnerAreaList.get(i)))) {
                auditAreaNum = i;
            }
        }

        if (auditContentNum == 0) {
            idAuditItem = (auditContentNum + 4) * 6 + auditClassNum * 6 + auditAreaNum - 24;
        } else {
            idAuditItem = (auditContentNum + 4) * 6 + auditAreaNum;
        }
    }

    private void addAuditItemDao(Long num) {
        if (auditItemList.size() > 0) {
            for (int i = 0; i < auditItemList.size(); i++) {
                auditItem.setId(0L+i+auditItemListBefore.size()+22);
                Log.d("HHH","auditItemListBefore"+auditItemListBefore.size());
                auditItem.setAuditItem(auditItemList.get(i));
                auditItem.setIdAuditItem(num);
                auditItemDao.insert(auditItem);
            }
        }
    }

    private void addItemList(String ss) {
        if ((ss != null) && (ss.length() > 0)) {
            auditItemList.add(ss);
        }
    }


    private boolean ifStringNull(String ss) {
        boolean kk = false;
        if ((ss != null) && (ss.length() > 0)) {
            kk = true;
        }
        return kk;
    }


    private void addAuditItemList1() {
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                stringAuditItem1 = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addAuditItemList2() {
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                stringAuditItem2 = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addAuditItemList3() {
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                stringAuditItem3 = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addAuditItemList4() {
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                stringAuditItem4 = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addAuditItemList5() {
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                stringAuditItem5 = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addAuditItemList6() {
        editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                stringAuditItem6 = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initData() {
        editText1 = findViewById(R.id.editText_superEditActivity01);
        editText2 = findViewById(R.id.editText_superEditActivity02);
        editText3 = findViewById(R.id.editText_superEditActivity03);
        editText4 = findViewById(R.id.editText_superEditActivity04);
        editText5 = findViewById(R.id.editText_superEditActivity05);
        editText6 = findViewById(R.id.editText_superEditActivity06);

        textView1 = findViewById(R.id.tv12);
        textView2 = findViewById(R.id.tv22);
        textView3 = findViewById(R.id.tv32);
        textView4 = findViewById(R.id.tv42);
        textView5 = findViewById(R.id.tv52);
        textView6 = findViewById(R.id.tv62);

        buttonFinish = findViewById(R.id.button_superEditActivity_finish);
        buttonQuit = findViewById(R.id.button_superEditActivity_quite);

        spinnerArea = findViewById(R.id.spinner_superEditActivity_area);
        spinnerClass = findViewById(R.id.spinner_superEditActivity_class);
        spinnerContent = findViewById(R.id.spinner_superEditActivity_content);

        auditItemList = new ArrayList<>();
        auditItemListBefore = new ArrayList<>();
    }

    private void initSpinnerArea() {
        spinnerAreaList = new ArrayList<>();
        spinnerAreaList.add("冲压");
        spinnerAreaList.add("焊接");
        spinnerAreaList.add("剪板");
        spinnerAreaList.add("工装");
        spinnerAreaList.add("设备");
        spinnerAreaList.add("物流");
        spinnerAreaList.add("请选择:审核范围");

        final MyAdapterLoginActivity arrAdapter2 = new MyAdapterLoginActivity(this, spinnerAreaList);
        spinnerArea.setAdapter(arrAdapter2);
        spinnerArea.setSelection(spinnerAreaList.size() - 1, true);

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                editText_auditAim.clearFocus();
//                editText_auditAim.setCursorVisible(false);
                auditArea = arrAdapter2.getItem(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpinnerClass() {
        spinnerClassList = new ArrayList<>();
        spinnerClassList.add("班组级");
        spinnerClassList.add("工段级");
        spinnerClassList.add("科室级");
        spinnerClassList.add("部门级");
        spinnerClassList.add("公司级");
        spinnerClassList.add("请选择:审核层级");

        final MyAdapterLoginActivity arrAdapter2 = new MyAdapterLoginActivity(this, spinnerClassList);
        spinnerClass.setAdapter(arrAdapter2);
        spinnerClass.setSelection(spinnerClassList.size() - 1, true);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void initSpinnerContent() {
        spinnerContentList = new ArrayList<>();
        spinnerContentList.add("分层审核");
        spinnerContentList.add("抽样检查");
        spinnerContentList.add("工作日志");
        spinnerContentList.add("安全检查");
        spinnerContentList.add("请选择:审核项目");

        final MyAdapterLoginActivity arrAdapter2 = new MyAdapterLoginActivity(this, spinnerContentList);
        spinnerContent.setAdapter(arrAdapter2);
        spinnerContent.setSelection(spinnerContentList.size() - 1, true);

        spinnerContent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                editText_auditAim.clearFocus();
//                editText_auditAim.setCursorVisible(false);
                auditContent = arrAdapter2.getItem(i).toString();

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
        auditItemDao = daoSession.getAuditItemDao();
        auditItem = new AuditItem();
    }

    private void getAuditItemListBefore(){
        if (auditItemDao!=null)
            auditItemListBefore = auditItemDao.queryBuilder().list();
    }

}
