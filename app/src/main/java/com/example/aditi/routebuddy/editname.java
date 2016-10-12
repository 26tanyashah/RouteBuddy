package com.example.aditi.routebuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class editname extends AppCompatActivity implements View.OnClickListener {


    EditText e_name;
    Button reset2;
    static String new_name;
    String uname;
    //String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editname);
        loginpage l2= new loginpage();
        uname=l2.getid();
        e_name=(EditText)findViewById(R.id.edit_name);
        reset2= (Button)findViewById(R.id.btnupdate2);
        reset2.setOnClickListener(this);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            reset2.setEnabled(false);
            //loginScreen.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        new_name=e_name.getText().toString();
        if(v.getId()==R.id.btnupdate2)
        {
            UpdateTask updateTask = new UpdateTask();
            updateTask.execute(uname,new_name);

        }
    }


    class UpdateTask extends AsyncTask<String,Void,Void> {
        String update_url;
        ProgressDialog loading;
        @Override
        protected  void onPreExecute()
        {
            update_url="http://routebuddy.comli.com/updatename.php";
            loading = ProgressDialog.show(editname.this,"Updating...","Wait...",false,false);
        }

        @Override
        protected Void doInBackground(String... args) {
            String loginid,n_name;

            loginid=args[0];
            n_name=args[1];

            String c=null;
            try {
                URL url = new URL(update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("loginid","UTF-8")+"="+URLEncoder.encode(loginid,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(n_name,"UTF-8");
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
            Toast.makeText(editname.this, "Name updated!!", Toast.LENGTH_SHORT).show();
            e_name.setText(" ");
        }
    }

}
