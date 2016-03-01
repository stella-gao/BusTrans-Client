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

    private String url = "http://128.235.29.191:8080/MyWebAppTest/Verify";

    //private String android_id = Secure.getString(getContext().getContentResolver(),Secure.ANDROID_ID);

    private String android_id = null;

    private boolean btnReqFlag = false;
    private Button mBtnLgn = null;
    private Button mBtnReq = null;

    private Spinner mORoute = null;
    private Spinner mOStop = null;
    private Spinner mDRoute = null;
    private Spinner mDStop = null;
    //private EditText mEtPwd = null;

    String oroute = null;//////////////////////
    String ostop = null;
    String droute = null;
    String dstop = null;
    //String password = null;


    Timer timer;
    TimerTask timerTask;

    private ArrayAdapter<CharSequence> adapterOStop = null;
    private ArrayAdapter<CharSequence> adapterDStop = null;
    String[][] dataStrings = { { "12", "13", "14", "58", "59", "60", "61" },
            {"14", "15", "16", }, { "1", "2", "3", "4", "5", "6", "7", "8", "9", "14", "15", "16", "17", "18", "19", "20", "21", "22", }, };





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
        mOStop = (Spinner)findViewById(R.id.oStop);
        mDRoute = (Spinner)findViewById(R.id.dRoute);
        mDStop = (Spinner) findViewById(R.id.dStop);
        //mEtPwd = (EditText)findViewById(R.id.etPwd);
        mBtnLgn = (Button) findViewById(R.id.btnLgn);
        mBtnReq = (Button) findViewById(R.id.btnReq);
        mBtnReq.setEnabled(false);
        //String colors[] = {"M5","M20","M21"};

       // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,colors);
       // mORoute.setAdapter(spinnerArrayAdapter);
       // mDRoute.setAdapter(spinnerArrayAdapter);

        mORoute.setOnItemSelectedListener(new OnItemSelectedListenerImploroute());
        mDRoute.setOnItemSelectedListener(new OnItemSelectedListenerImpldroute());




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
               // ostop = mOStop.getText().toString();
                ostop = mOStop.getSelectedItem().toString();
                droute = mDRoute.getSelectedItem().toString();
                dstop= mDStop.getSelectedItem().toString();
                //dstop = mDStop.getText().toString();

                // password = mEtPwd.getText().toString();
                if (oroute.equals("") || oroute == null) {
                    Toast.makeText(Login.this, "Please input your origin route.",
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
                } else if (dstop.equals("") || dstop == null) {
                    Toast.makeText(Login.this, "Please input your destination stop.",
                            Toast.LENGTH_SHORT).show();
                    //return;
                } else {
                    new Thread(verifyThread).start();

                }
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
    Handler httpHandler = new Handler(){
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
            delay.setText("The delay time and related information are as follows:\n"+ val);


            btnReqFlag = true;
            mBtnReq.setEnabled(true);
            mBtnReq.setClickable(btnReqFlag);
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







    /**
     * 网络访问线程，功能为验证用户身份
     */
    Runnable verifyThread = new Runnable(){
        @Override
        public void run() {
            // TODO: http post.
            String result = "-1";
            //noinspection deprecation
            //HttpClient httpClient = new DefaultHttpClient();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //String url = "https://web.njit.edu/~yl768/webapps7/Verify";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);
            NameValuePair pair2 = new BasicNameValuePair("orn", oroute);
            NameValuePair pair3 = new BasicNameValuePair("ostop", ostop);
            NameValuePair pair4 = new BasicNameValuePair("drn", droute);
            NameValuePair pair5 = new BasicNameValuePair("dstop", dstop);

            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(pair1);
            pairs.add(pair2);
            pairs.add(pair3);
            pairs.add(pair4);
            pairs.add(pair5);




            for (int i=0; i<100; i++) {
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
                data.putString("delayvalue", result);
                msg.setData(data);
                httpHandler.sendMessage(msg);
                /////////////////
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable successThread = new Runnable(){
        @Override
        public void run() {
            // TODO: http post.
            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);
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


