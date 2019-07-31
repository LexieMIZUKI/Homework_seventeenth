package com.lexieluv.homeworkseventeenth;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText et_search;
    private ListView lv_content;
    private List<AppInfo> data;//所有应用的数据列表
    private AppAdapter appAdapter;
    private ImageView iv_cancel;

    private List<String> title;//创造一个放名字的列表，方便进行搜索

    Handler myhandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化
        listener();//所有监听操作

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            super.onSaveInstanceState(outState);
        }
    }

    //UI操作，改变数据并绑定在listview上面
    Runnable eChanged = new Runnable() {
        @Override
        public void run() {
            String nData = et_search.getText().toString();
//            data.clear();
            getNewData(data,nData);
            appAdapter.notifyDataSetChanged();
        }
    };

    //进行对输入框内容的判断
    private void getNewData(List<AppInfo> data, String nData) {
        int length = title.size();
        List<AppInfo> sData = new ArrayList<>();
        this.data = data;
        for(int i = 0;i < length;i++){
            if(title.get(i).contains(nData)){
                sData.add(data.get(i));
            }
        }
        data.clear();
        for(int i = 0;i < sData.size();i++){
            data.add(sData.get(i));
        }
    }

    private void listener() {
        //点击输入法的搜索时的监听
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                   //TODO:点击搜索要做的操作
                    myhandle.post(eChanged);
                }
                return false;
            }
        });

        //输入文字的监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    iv_cancel.setVisibility(View.GONE);
                } else {
                    iv_cancel.setVisibility(View.VISIBLE);
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //提示当前行的应用名称
                String appName = data.get(position).getName();
                //提示
                Toast.makeText(MainActivity.this,appName,Toast.LENGTH_SHORT).show();
            }
        });

//        //给listview设置item的长按监听
//        lv_content.setOnItemLongClickListener(this);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                data = getAllInfos();
                appAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        et_search = findViewById(R.id.et_search);
        lv_content = findViewById(R.id.lv_content);
        iv_cancel = findViewById(R.id.iv_cancel);

        data = getAllInfos();

        appAdapter = new AppAdapter();
        lv_content.setAdapter(appAdapter);

        //专门一个存放名字的列表初始化
        title = new ArrayList<>();
        for(int i = 0; i < data.size();i++){
            title.add(data.get(i).getName());
        }
    }

    //获得data的数据
    protected List<AppInfo> getAllInfos(){
        List<AppInfo> list = new ArrayList<>();
        //重点！！得到应用的packageManager
        PackageManager packageManager = getPackageManager();
        //创建一个主界面的intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //得到包含应用信息的列表
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent,0);
        //遍历
        for(ResolveInfo r : resolveInfos){
            //得到包名
            String pName = r.activityInfo.packageName;
            //得到图标
            Drawable icon = r.loadIcon(packageManager);
            //得到应用名称
            String name = r.loadLabel(packageManager).toString();
            //封装应用信息对象
            AppInfo appInfo = new AppInfo(icon,name,pName);
            //添加到list
            list.add(appInfo);
        }
        return list;
    }

    //listview的适配器
    class AppAdapter extends BaseAdapter implements Serializable {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(MainActivity.this,R.layout.item_layout,null);
            }
            AppInfo appInfo = data.get(position);
            ImageView imageView = convertView.findViewById(R.id.item_iv);
            TextView textView = convertView.findViewById(R.id.item_tv);
            imageView.setImageDrawable(appInfo.getImg());
            textView.setText(appInfo.getName());
            return convertView;
        }
    }
}


