package com.example.aditi.routebuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class editemail extends AppCompatActivity implements View.OnClickListener {


    EditText e_email;
    Button reset1;
    static String new_email;
    String uname;
    // String JSON_STRING;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginpage l1= new loginpage();
        uname=l1.getid();
        setContentView(R.layout.activity_editemail);
        e_email=(EditText)findViewById(R.id.edit_email);
        reset1= (Button)findViewById(R.id.btnupdate1);
        reset1.setOnClickListener(this);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            reset1.setEnabled(false);
            //loginScreen.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {

        new_email=e_email.getText().toString();
        if(v.getId()==R.id.btnupdate1)
        {
            UpdateTask updateTask = new UpdateTask();
            updateTask.execute(uname,new_email);

        }
    }





    class UpdateTask extends AsyncTask<String,Void,Void> {
        String update_url;
        ProgressDialog loading;
        @Override
        protected  void onPreExecute()
        {
            update_url="http://routebuddy.comli.com/updateemail.php";
            loading = ProgressDialog.show(editemail.this,"Updating...","Wait...",false,false);
        }

        @Override
        protected Void doInBackground(String... args) {
            String loginid,n_email;

            loginid=args[0];
            n_email=args[1];

            String c=null;
            try {
                URL url = new URL(update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("loginid","UTF-8")+"="+URLEncoder.encode(loginid,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(n_email,"UTF-8");
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
            Toast.makeText(editemail.this, "Email updated!!", Toast.LENGTH_SHORT).show();
            e_email.setText(" ");
        }
    }

}
