package com.example.aditi.routebuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TabActivity_1 extends Activity implements View.OnClickListener {

    Button btn_editinfo;
    Button btn_chagepass;
    Button btn_logout;
    String uname;
    TextView header;
    TextView one_f;
    TextView sec_f;
    TextView third_f;
    TextView edit_1;
    TextView edit_2;
    TextView edit_3;


    String JSON_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_activity_1);
        header = (TextView) findViewById(R.id.wlcmheader);
        one_f=(TextView)findViewById(R.id.textView6);
        sec_f=(TextView)findViewById(R.id.textView7);
        third_f=(TextView)findViewById(R.id.textView8);
        edit_1=(TextView)findViewById(R.id.editname);
        edit_2=(TextView)findViewById(R.id.editemail);
        edit_3=(TextView)findViewById(R.id.editphone);


        // Bundle extras = getIntent().getExtras();

        // if (extras != null) {
        //   header.setText("WELCOME "); /*+ extras.getString("uname");*/
        //}

        //Intent i1 = getIntent();
        //uname = i1.getStringExtra("uname");

        loginpage t= new loginpage();
        uname= t.getid();
        header.setText("WELCOME "+uname);

        btn_editinfo = (Button) findViewById(R.id.btneditINFO);

        btn_chagepass = (Button) findViewById(R.id.btchangePASS);

        btn_logout = (Button) findViewById(R.id.btLOGOUT);

        btn_editinfo.setOnClickListener(this);

        btn_chagepass.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        edit_1.setOnClickListener(this);
        edit_2.setOnClickListener(this);
        edit_3.setOnClickListener(this);

        new BackgroundTask().execute();


    }





    @Override
    public void onClick(View v) {
        //onClick();

        switch (v.getId()){
            case R.id.editemail:
                Intent ii1= new Intent(this,editemail.class);
                startActivity(ii1);

                break;
            case R.id.editname:

                Intent ii2= new Intent(this,editname.class);
                startActivity(ii2);
                break;
            case R.id.editphone:
                Intent ii3= new Intent(this,editphone.class);
                startActivity(ii3);

                break;

            case R.id.btneditINFO:


                break;
            case R.id.btchangePASS:

                Intent i2= new Intent(this,changepass.class);
                i2.putExtra("uname_cp",uname);
                startActivity(i2);

                break;
            case R.id.btLOGOUT:
                // signOut();

                break;
            default:
                Toast.makeText(this, "error!!", Toast.LENGTH_SHORT).show();

                break;
        }



    }
    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String json_url3;



        @Override
        protected  void onPreExecute()
        {
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url3= "http://routebuddy.comli.com/display.php?userid="+uname;

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url3);
                Log.d("b1","b1");
                Log.d("conn","conn");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream2 = httpURLConnection.getInputStream();
                BufferedReader bufferedReader2=new BufferedReader(new InputStreamReader(inputStream2));
                StringBuilder stringBuilder2= new StringBuilder();
                //JSON_STRING=new HashMap();
                Log.d("conn2","conn2");

                while((JSON_STRING = bufferedReader2.readLine())!=null)
                {
                    stringBuilder2.append(JSON_STRING+"\n");
                    Log.d("e1","e1");
                }

                bufferedReader2.close();
                inputStream2.close();
                httpURLConnection.disconnect();
                Log.d("f1","f1");
                return stringBuilder2.toString().trim();


            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }





        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("d1,","d1");
            // boolean b1=false;
            String json_string;
            JSONObject jsonObject;
            JSONArray jsonArray;

            String name_a;
            String email_a;
            String phone_a;
            json_string=result;
            Log.d("uu: ",result);
            //get_json gj=new get_json();
            //gj.check_login(username,password);
            try{
                Log.d("x1","x1");
                jsonObject=new JSONObject(json_string);
                jsonArray=jsonObject.getJSONArray("server_response");
                //int count=0;
                // Log.d("c","c");
                //while (count<jsonArray.length())
                //{
                Log.d("c1","c1");
                JSONObject JOO2=jsonArray.getJSONObject(0);
                name_a=JOO2.getString("name");
                email_a=JOO2.getString("email");
                phone_a=Integer.toString(JOO2.getInt("phone"));
                one_f.setText(name_a);
                sec_f.setText(email_a);
                third_f.setText(phone_a);



            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }


}