package com.example.aditi.routebuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class loginpage extends Activity implements View.OnClickListener{
    String JSON_STRING;
    EditText editText1;
    EditText editText2;
    Button button;
    int attempt_counter=3;
    static String username;
    static String password;
    String json_string;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        button=(Button)findViewById(R.id.btnLogin);
        editText1=(EditText) findViewById(R.id.editText1);
        editText2=(EditText) findViewById(R.id.editText2);
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), registeractivity.class);
                startActivity(i);
            }
        });
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            button.setEnabled(false);
            registerScreen.setEnabled(false);
        }

        //button.setOnClickListener(new View.OnClickListener() {

        //  public void onClick(View v) {

        //    Intent i = new Intent(getApplicationContext(), main.class);
        //  startActivity(i);
        //}
        //});
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        username=editText1.getText().toString();
        password=editText2.getText().toString();
        if(username.isEmpty() || password.isEmpty())
        {
            if(username.isEmpty())
                Toast.makeText(this,"enter username",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,"enter password",Toast.LENGTH_LONG).show();
        }
        else
        {
            new BackgroundTask().execute();
            /*Intent i=new Intent(this,get_json.class);
            i.putExtra("json_data",json_string);
            startActivity(i);*/
            // DBhelper db=new DBhelper(this);
            //int b = db.checklogin(username,password);
            //Toast.makeText(this,"incorrect",Toast.LENGTH_LONG).show();
           /* if(b == 1 )
            {
                Intent i=new Intent(this,main.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this,"incorrect username or password",Toast.LENGTH_LONG).show();
                editText1.setText("");
                editText2.setText("");
            }
            */
        }
    }

    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String json_url;


        @Override
        protected  void onPreExecute()
        {
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url= "http://routebuddy.comli.com/json_get_data.php";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                Log.d("a","a");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder= new StringBuilder();
                //JSON_STRING=new HashMap();
                while((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

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

            boolean b1=false;
            String json_string;
            JSONObject jsonObject;
            JSONArray jsonArray;
            String loginid;
            String pass ;
            json_string=result;
            //get_json gj=new get_json();
            //gj.check_login(username,password);
            try{
                jsonObject=new JSONObject(json_string);
                jsonArray=jsonObject.getJSONArray("server_response");
                int count=0;
                Log.d("c","c");
                while (count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    loginid=JO.getString("loginid");
                    pass=JO.getString("password");

                    // call the dcryption function ans save in dcrypt pass n pass it

                    //name=JO.getString("name");
                    //email=JO.getString("email");
                    Log.d(loginid,pass);
                    //phone=Integer.parseInt(JO.getString("phone"));
                    if(loginid.equals(username) && pass.equals(password))
                    {
                        b1=true;
                        Log.d("b","b");
                        Intent i1 = new Intent(loginpage.this, main.class);
                        startActivity(i1);
                        break;
                    }
                    count++;
                }
                if(b1==false)
                {
                    attempt_counter--;
                    Toast.makeText(loginpage.this,"attempts left "+attempt_counter,Toast.LENGTH_SHORT).show();
                    if(attempt_counter==0)
                    {
                        Toast.makeText(loginpage.this,"no attempts left!!",Toast.LENGTH_SHORT).show();
                        button.setEnabled(false);
                        finish();
                    }
                    Toast.makeText(loginpage.this,"incorrect username or password",Toast.LENGTH_LONG).show();
                    editText1.setText("");
                    editText2.setText("");
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }
    public String getid()
    {
        return username;
    }
}
