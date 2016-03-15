package com.example.yuan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuan.smartbus.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.CloseableHttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.CloseableHttpClient;
import ch.boye.httpclientandroidlib.impl.client.HttpClients;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import android.provider.Settings.Secure;
//
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;


import android.widget.AdapterView.OnItemSelectedListener;






/**
 * Login Activity
 */
public class Login extends Activity {

    private String urlinput = "http://128.235.163.180:8080/MyWebAppTest/TestInput";
    private String urlhold = "http://128.235.163.180:8080/MyWebAppTest/TestHold";
    private String urlquery = "http://128.235.163.180:8080/MyWebAppTest/TestQuery";
    //private String android_id = Secure.getString(getContext().getContentResolver(),Secure.ANDROID_ID);

    private String android_id = null;

    private boolean btnReqFlag = false;
    private Button mBtnLgn = null;
    private Button mBtnDly = null;
    private Button mBtnReq = null;

    private Spinner mORoute = null;
    private Spinner mOStop = null;
    private Spinner mODir = null;

    private Spinner mDRoute = null;
    private Spinner mDStop = null;
    private Spinner mDDir = null;

    //private EditText mEtPwd = null;

    ArrayAdapter<String> orouteAdapter = null;  //省级适配器
    ArrayAdapter<String> odirAdapter = null;    //地级适配器
    ArrayAdapter<String> ostopAdapter = null;    //县级适配器

    ArrayAdapter<String> drouteAdapter = null;  //省级适配器
    ArrayAdapter<String> ddirAdapter = null;    //地级适配器
    ArrayAdapter<String> dstopAdapter = null;    //县级适配器

    String oroute = null;//////////////////////
    String odir = null;
    String ostop = null;

    String droute = null;
    String ddir = null;
    String dstop = null;
    //String password = null;

    static int oroutePosition = 1;
    static int droutePosition = 1;


    Timer timer;
    TimerTask timerTask;


    //省级选项值
    private String[] soroute = new String[] {"M5","M20","M21"};
    //地级选项值
    private String[][] sodir = new String[][]
            {
                    { "N", "S" },
                    { "N" },
                    { "E", "W" }
            };

    //县级选项值
    private String[][][] sostop = new String[][][]
            {
                    {   //北京
                            {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"},
                            {"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65"}
                    },
                    {    //上海
                            {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}
                    },
                    {    //天津
                            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"},
                            {"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"}
                    }
            };


    //省级选项值
    private String[] sdroute = new String[] {"M5","M20","M21"};
    //地级选项值
    private String[][] sddir = new String[][]
            {
                    { "N", "S" },
                    { "N" },
                    { "E", "W" }
            };

    //县级选项值
    private String[][][] sdstop = new String[][][]
            {
                    {   //北京
                            {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"},
                            {"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65"}
                    },
                    {    //上海
                            {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}
                    },
                    {    //天津
                            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"},
                            {"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"}
                    }
            };


    private ArrayAdapter<CharSequence> adapterOStop = null;
    private ArrayAdapter<CharSequence> adapterDStop = null;
    String[][] dataStrings = { { "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63",},
            {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", },
            { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", }, };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        android_id = Secure.getString(Login.this.getContentResolver(),Secure.ANDROID_ID);
        //System.out.println(android_id);
        setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    /**
     * Initial button members
     */
    private void initView(){
        mORoute = (Spinner)findViewById(R.id.oRoute);
        mODir = (Spinner)findViewById(R.id.oDir);
        mOStop = (Spinner)findViewById(R.id.oStop);

        mDRoute = (Spinner)findViewById(R.id.dRoute);
        mDDir = (Spinner)findViewById(R.id.dDir);
        mDStop = (Spinner) findViewById(R.id.dStop);

        orouteAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, soroute);
        mORoute.setAdapter(orouteAdapter);
        mORoute.setSelection(1,true);  //设置默认选中项，此处为默认选中第1个值

        drouteAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sdroute);
        mDRoute.setAdapter(drouteAdapter);
        mDRoute.setSelection(1,true);  //设置默认选中项，此处为默认选中第2个值

        odirAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sodir[1]);
        mODir.setAdapter(odirAdapter);
        mODir.setSelection(0,true);  //默认选中第0个

        ddirAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sddir[1]);
        mDDir.setAdapter(ddirAdapter);
        mDDir.setSelection(0,true);  //默认选中第0个

        ostopAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sostop[1][0]);
        mOStop.setAdapter(ostopAdapter);
        mOStop.setSelection(0, true);

        dstopAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sdstop[1][0]);
        mDStop.setAdapter(dstopAdapter);
        mDStop.setSelection(0, true);



        //mEtPwd = (EditText)findViewById(R.id.etPwd);
        mBtnLgn = (Button) findViewById(R.id.btnLgn);
        mBtnDly = (Button) findViewById(R.id.btnDly);
        mBtnReq = (Button) findViewById(R.id.btnReq);
        mBtnReq.setEnabled(false);
        //String colors[] = {"M5","M20","M21"};

       // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,colors);
       // mORoute.setAdapter(spinnerArrayAdapter);
       // mDRoute.setAdapter(spinnerArrayAdapter);

       // mORoute.setOnItemSelectedListener(new OnItemSelectedListenerImploroute());
       // mDRoute.setOnItemSelectedListener(new OnItemSelectedListenerImpldroute());

        //省级下拉框监听
        mORoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //position为当前省级选中的值的序号

                //将地级适配器的值改变为city[position]中的值
                odirAdapter = new ArrayAdapter<String>(
                        Login.this, android.R.layout.simple_spinner_item, sodir[position]);
                // 设置二级下拉列表的选项内容适配器
                mODir.setAdapter(odirAdapter);
                oroutePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }

        });


        //地级下拉监听
        mODir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3)
            {
                ostopAdapter = new ArrayAdapter<String>(Login.this,
                        android.R.layout.simple_spinner_item, sostop[oroutePosition][position]);
                mOStop.setAdapter(ostopAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        //省级下拉框监听
        mDRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //position为当前省级选中的值的序号

                //将地级适配器的值改变为city[position]中的值
                ddirAdapter = new ArrayAdapter<String>(
                        Login.this, android.R.layout.simple_spinner_item, sddir[position]);
                // 设置二级下拉列表的选项内容适配器
                mDDir.setAdapter(ddirAdapter);
                droutePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }

        });


        //地级下拉监听
        mDDir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3)
            {
                dstopAdapter = new ArrayAdapter<String>(Login.this,
                        android.R.layout.simple_spinner_item, sdstop[droutePosition][position]);
                mDStop.setAdapter(dstopAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });




    }

    private class OnItemSelectedListenerImploroute implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            Login.this.adapterOStop = new ArrayAdapter<CharSequence>(
                    Login.this, android.R.layout.simple_spinner_item,
                    Login.this.dataStrings[arg2]); // 定义所有的列表项
            Login.this.mOStop.setAdapter(Login.this.adapterOStop); // 设置二级列表选项框的内容
            Login.this.adapterOStop
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉框列表样式
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {// 表示没有选项的时候触发
            // 一般此方法现在不关心
        }

    }

    private class OnItemSelectedListenerImpldroute implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            Login.this.adapterDStop = new ArrayAdapter<CharSequence>(
                    Login.this, android.R.layout.simple_spinner_item,
                    Login.this.dataStrings[arg2]); // 定义所有的列表项
            Login.this.mDStop.setAdapter(Login.this.adapterDStop); // 设置二级列表选项框的内容
            Login.this.adapterDStop
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉框列表样式
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {// 表示没有选项的时候触发
            // 一般此方法现在不关心
        }

    }



    /**
     * Set Button Listeners
     */
    private void setListener(){
        mBtnLgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oroute = mORoute.getSelectedItem().toString();
                odir = mODir.getSelectedItem().toString();
                ostop = mOStop.getSelectedItem().toString();


                droute = mDRoute.getSelectedItem().toString();
                ddir = mDDir.getSelectedItem().toString();
                dstop= mDStop.getSelectedItem().toString();
                //dstop = mDStop.getText().toString();

                // password = mEtPwd.getText().toString();
                if (oroute.equals("") || oroute == null) {
                    Toast.makeText(Login.this, "Please input your origin route.",
                            Toast.LENGTH_SHORT).show();
                    //return;
                }else if (odir.equals("") || odir == null) {
                    Toast.makeText(Login.this, "Please input your origin direction.",
                            Toast.LENGTH_SHORT).show();
                    //return;
                } else if (ostop.equals("") || ostop == null) {
                    Toast.makeText(Login.this, "Please input your origin stop.",
                            Toast.LENGTH_SHORT).show();
                    //return;
                } else if (droute.equals("") || droute == null) {
                    Toast.makeText(Login.this, "Please input your destination route.",
                            Toast.LENGTH_SHORT).show();
                    //return;
                } else if (ddir.equals("") || ddir == null) {
                    Toast.makeText(Login.this, "Please input your destination direction.",
                            Toast.LENGTH_SHORT).show();
                    //return;
                } else if (dstop.equals("") || dstop == null) {
                    Toast.makeText(Login.this, "Please input your destination stop.",
                            Toast.LENGTH_SHORT).show();
                    //return;
                } else {
                    new Thread(inputThread).start();

                }
            }
        });

        mBtnDly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //:TODO

                new Thread(delayThread).start();

                }
        });

        mBtnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //:TODO
                //holdsuccess = holdsuccess.getText().toString();

/*
                if (holdsuceess.equals("1")) {
                    Toast.makeText(Login.this, "Success hold your request!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Login.this, "Not success hold your request!",
                            Toast.LENGTH_LONG).show();
                }*/


                dialog();
                //new Thread(successThread).start();


            }
        });
    }


    protected void dialog() {
         // AlertDialog.Builder builder = new Builder(Login.this);
        //定义一个AlertDialog.Builder对象
        final Builder builder = new AlertDialog.Builder(this);

          builder.setMessage("Are you sure to hold the request？");
          builder.setTitle("You clicked the request button");
          builder.setPositiveButton("Hold Request", new OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                    new Thread(successThread).start();
                    dialog.dismiss();
                    //Login.this.finish();
                   }
              });
          builder.setNegativeButton("Cancel", new OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                   }
              });
          builder.create().show();
         }


    /**
     * 网络访问线程甩出的句柄，可以在这里做UI操作
     */
    Handler httpHandlerInput = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("input");
            //Log.d("Http","请求结果:" + val);
            //可以开始处理UI
            //Toast.makeText(Login.this, "The result is " + val,
            //        Toast.LENGTH_LONG).show();

            if(val.equals("1")) {
                Toast.makeText(Login.this, "We have got your information!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Login.this, "Sorry. Fail to connect server. Please try later.",
                        Toast.LENGTH_LONG).show();
            }

            /*Toast.makeText(Login.this, "Delay time is "+val,
                    Toast.LENGTH_LONG).show();*/

            btnReqFlag = true;
            mBtnReq.setEnabled(true);
            mBtnReq.setClickable(btnReqFlag);
        }
    };


    /**
     * 网络访问线程甩出的句柄，可以在这里做UI操作
     */
    Handler httpHandlerDly = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("delayvalue");
            //Log.d("Http","请求结果:" + val);
            //可以开始处理UI
            //Toast.makeText(Login.this, "The result is " + val,
            //        Toast.LENGTH_LONG).show();
           /*
            if(val.equals(oroute+" is in the route list")) {
                Toast.makeText(Login.this, "Your chosen route " + oroute +" is in the route list!",
                        Toast.LENGTH_LONG).show();

            } else if (val.equals(oroute+" is not in the route list")){
                Toast.makeText(Login.this, "Wrong route information or/and format.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Login.this, "Sorry. Fail to connect server. Please try later.",
                        Toast.LENGTH_LONG).show();
            }
            */
            /*Toast.makeText(Login.this, "Delay time is "+val,
                    Toast.LENGTH_LONG).show();*/


            final TextView delay = (TextView)findViewById(R.id.delay);
            delay.setText("The delay time and related info are:\n" + val);

/*
            btnReqFlag = true;
            mBtnReq.setEnabled(true);
            mBtnReq.setClickable(btnReqFlag);*/
        }
    };

    Handler httpHandlerReq = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            //Log.d("Http","请求结果:" + val);
            //可以开始处理UI
            //Toast.makeText(Login.this, "The result is " + val,
            //        Toast.LENGTH_LONG).show();
           /*
            if(val.equals(oroute+" is in the route list")) {
                Toast.makeText(Login.this, "Your chosen route " + oroute +" is in the route list!",
                        Toast.LENGTH_LONG).show();

            } else if (val.equals(oroute+" is not in the route list")){
                Toast.makeText(Login.this, "Wrong route information or/and format.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Login.this, "Sorry. Fail to connect server. Please try later.",
                        Toast.LENGTH_LONG).show();
            }
            */
           /* Toast.makeText(Login.this, "Hold Success is "+val,
                    Toast.LENGTH_LONG).show();*/


            final TextView success = (TextView)findViewById(R.id.success);
            if (val=="1")  success.setText("Success hold your request.");
            else success.setText("We cannot hold your request.");


        }
    };



    Runnable inputThread = new Runnable(){

        @Override
        public void run() {
            // TODO: http post.
            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(urlinput);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);
            NameValuePair pair2 = new BasicNameValuePair("orn", oroute);
            NameValuePair pair3 = new BasicNameValuePair("poDir", odir);
            NameValuePair pair4 = new BasicNameValuePair("ostop", ostop);
            NameValuePair pair5 = new BasicNameValuePair("drn", droute);
            NameValuePair pair6 = new BasicNameValuePair("ptDir", ddir);
            NameValuePair pair7 = new BasicNameValuePair("dstop", dstop);

            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs= new ArrayList<>();
            pairs.add(pair1);
            pairs.add(pair2);
            pairs.add(pair3);
            pairs.add(pair4);
            pairs.add(pair5);
            pairs.add(pair6);
            pairs.add(pair7);

            try {
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(requestEntity);
                try {
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {

                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        result = reader.readLine();
                    } else {
                        result = "" + response.getStatusLine().getStatusCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("input",result);
            msg.setData(data);
            httpHandlerInput.sendMessage(msg);
        }
    };




    /**
     * 网络访问线程，功能为验证用户身份
     */

   // private class delayThread implements Runnable {

    Runnable delayThread = new Runnable(){

        @Override
        public void run() {

            // TODO: http post.
            /*
            final String[] result = {"-1"};
            final CloseableHttpClient httpClient = HttpClients.createDefault();
            final HttpPost httpPost = new HttpPost(urlquery);*/

            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(urlquery);
            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);

            //将准备好的键值对对象放置在一个List当中
           final ArrayList<NameValuePair> pairs= new ArrayList<>();
           pairs.add(pair1);


           // Timer timer = new Timer();
            //timer.schedule(new TimerTask() {
            //    @Override
            //    public void run() {
                    try {
                        //创建代表请求体的对象（注意，是请求体）
                       // HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                        HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                        //将请求体放置在请求对象当中
                        httpPost.setEntity(requestEntity);
                        //执行请求对象
                        try {
                            //第三步：执行请求对象，获取服务器返还的相应对象
                            CloseableHttpResponse response = httpClient.execute(httpPost);
                            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                            if (response.getStatusLine().getStatusCode() == 200) {
                                //第五步：从相应对象当中取出数据，放到entity当中
                                HttpEntity entity = response.getEntity();
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(entity.getContent()));
                               // result[0] = reader.readLine();
                                result = reader.readLine();
                            } else {
                                //result[0] = "" + response.getStatusLine().getStatusCode();
                                result = "" + response.getStatusLine().getStatusCode();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    //data.putString("delayvalue", result[0]);
                    data.putString("delayvalue", result);
                    msg.setData(data);
                    httpHandlerDly.sendMessage(msg);
            ///    }
           // }, 0, 100);

        }
    };

    Runnable successThread = new Runnable(){

        @Override
        public void run() {
            // TODO: http post.
            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(urlhold);
            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);
            NameValuePair pair2 = new BasicNameValuePair("p_hold_request", "1");


            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(pair1);
            pairs.add(pair2);

            try {
                //创建代表请求体的对象（注意，是请求体）
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                //将请求体放置在请求对象当中
                httpPost.setEntity(requestEntity);
                //执行请求对象
                try {
                    //第三步：执行请求对象，获取服务器返还的相应对象
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        result = reader.readLine();
                        //Log.d("HTTP", "POST:" + result);
                    } else {
                        result = "" + response.getStatusLine().getStatusCode();
                        //Log.d("HTTP", "ERROR:" + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value",result);
            msg.setData(data);
            httpHandlerReq.sendMessage(msg);
        }
    };

}


