package com.example.aditi.routebuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;

public class changepass extends AppCompatActivity implements View.OnClickListener {

    String userid;
    String JSON_STRING;
    EditText newpass;
    EditText v_newpass;
    EditText curr_pass;
    TextView passstrength;
    static String n_pass;
    static String v_npass;
    static String c_pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        //Intent i2=getIntent();
        //userid= i2.getStringExtra("uname_cp");
        loginpage l=new loginpage();
        userid=l.getid();
        Button changepass= (Button)findViewById(R.id.btn_cp);
        curr_pass=(EditText)findViewById(R.id.password_cp);
        newpass=(EditText)findViewById(R.id.password_np);
        v_newpass=(EditText)findViewById(R.id.password_npv);


        // changepass.setOnClickListener(this);
        changepass.setOnClickListener(this);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            changepass.setEnabled(false);
            //loginScreen.setEnabled(false);
        }

        newpass.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (KeyEvent.KEYCODE_TAB == keyCode) {


                    updatePasswordStrengthView(newpass.getText().toString());


                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {

        c_pass=curr_pass.getText().toString();

        n_pass=newpass.getText().toString();
        v_npass= v_newpass.getText().toString();

        if(v.getId()==R.id.btn_cp)
        {
            Log.d("a1","a1");
            new BackgroundTask().execute();

        }
    }

    private void updatePasswordStrengthView(String password) {

        passstrength = (TextView)findViewById(R.id.password_strength);


        PasswordStrength str = PasswordStrength.calculateStrength(password);
        passstrength.setText(MessageFormat.format(
                getText(R.string.password_strength_caption).toString(),
                str.getText(this)));
        passstrength.setTextColor(str.getColor());
    }

    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String json_url2;



        @Override
        protected  void onPreExecute()
        {
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url2= "http://routebuddy.comli.com/json_change_pass.php?userid="+userid;

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url2);
                Log.d("b1","b1");
                Log.d("conn","conn");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream1 = httpURLConnection.getInputStream();
                BufferedReader bufferedReader1=new BufferedReader(new InputStreamReader(inputStream1));
                StringBuilder stringBuilder1= new StringBuilder();
                //JSON_STRING=new HashMap();
                Log.d("conn2","conn2");

                while((JSON_STRING = bufferedReader1.readLine())!=null)
                {
                    stringBuilder1.append(JSON_STRING+"\n");
                    Log.d("e1","e1");
                }

                bufferedReader1.close();
                inputStream1.close();
                httpURLConnection.disconnect();
                Log.d("f1","f1");
                return stringBuilder1.toString().trim();


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

            String pass ;
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
                JSONObject JOO=jsonArray.getJSONObject(0);
                //loginid=JO.getString("loginid");
                pass=JOO.getString("password");
                //name=JO.getString("name");
                //email=JO.getString("email");
                //Log.d(loginid,pass);
                //phone=Integer.parseInt(JO.getString("phone"));


                //Decrypt
                if(pass.equals(c_pass))
                {
                    // b1=true;
                    //Log.d("b","b");
                    if(!n_pass.equals(v_npass))
                    {
                        Toast.makeText(changepass.this,"new passwords do not match!!",Toast.LENGTH_LONG).show();
                        newpass.setText("");
                        v_newpass.setText("");

                    }
                    else
                    {

                        //ENCRYPT

                        UpdateTask updateTask = new UpdateTask();
                        updateTask.execute(userid,n_pass);
                    }



                }

                else
                {
                    Toast.makeText(changepass.this, "you entered wrong current password!!", Toast.LENGTH_LONG).show();
                }
                //count++;
                //}

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }



    class UpdateTask extends AsyncTask<String,Void,Void> {
        String update_url;
        ProgressDialog loading;
        @Override
        protected  void onPreExecute()
        {
            update_url="http://routebuddy.comli.com/update.php";
            loading = ProgressDialog.show(changepass.this,"Updating...","Wait...",false,false);
        }

        @Override
        protected Void doInBackground(String... args) {
            String loginid,password;

            loginid=args[0];
            password=args[1];

            String c=null;
            try {
                URL url = new URL(update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("loginid","UTF-8")+"="+URLEncoder.encode(loginid,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
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
        protected void onPostExecute(Void aVoid) {
            loading.dismiss();
        }
    }


}
