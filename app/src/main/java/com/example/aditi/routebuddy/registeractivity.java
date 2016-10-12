package com.example.aditi.routebuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registeractivity extends Activity implements View.OnClickListener{


    TextView loginScreen;
    TextView strengthView;

    EditText login1;
    EditText phone1;
    EditText email1;
    EditText pass1;
    EditText pass2;
    EditText name1;
    static String login;
    static long phone;
    static String email;
    static String pass;
    static String passr;
    static String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registeractivity);
        Button button=(Button)findViewById(R.id.btnRegister);
        loginScreen = (TextView) findViewById(R.id.link_to_login);
        login1=(EditText) findViewById(R.id.reg_username);
        phone1=(EditText) findViewById(R.id.reg_phone);
        pass1=(EditText) findViewById(R.id.reg_password);
        pass2=(EditText) findViewById(R.id.reg_password1);
        name1=(EditText) findViewById(R.id.edit_phone);
        email1=(EditText) findViewById(R.id.reg_email);

        loginScreen.setOnClickListener(this);

        pass1.setOnKeyListener(new View.OnKeyListener() {

                                   @Override
                                   public boolean onKey(View v, int keyCode, KeyEvent event) {

                                       if (KeyEvent.KEYCODE_TAB == keyCode) {


                                           updatePasswordStrengthView(pass1.getText().toString());


                                       }

                                       return false;
                                   }
                               });
        button.setOnClickListener(this);
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            button.setEnabled(false);
            loginScreen.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {

            String temp;
        Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
        login=login1.getText().toString();
        email=email1.getText().toString();
        name=name1.getText().toString();
        pass=pass1.getText().toString();
        passr=pass2.getText().toString();
        temp=phone1.getText().toString();
        phone=Long.parseLong(phone1.getText().toString());
        switch(v.getId()) {
            case R.id.link_to_login:
                Intent mainIntent = new Intent(registeractivity.this, com.example.aditi.routebuddy.loginpage.class);
                registeractivity.this.startActivity(mainIntent);
                break;
            case R.id.btnRegister:
                Matcher matcher1 = pattern1.matcher(email);

                if(!pass.equals(passr) || !matcher1.matches()|| pass.isEmpty() || passr.isEmpty() || email.isEmpty()|| login.isEmpty()||name.isEmpty()||temp.isEmpty())
                {
                    if(pass.isEmpty() || passr.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter password, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(name.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter name, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(login.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter loginid, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(temp.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter contact, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(email.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter email, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(!pass.equals(passr)) {
                        Toast err = Toast.makeText(registeractivity.this, "sorry! passwords do not match", Toast.LENGTH_SHORT);
                        err.show();
                        // Log.d("pass1","pass2");
                        pass1.setText("");
                        pass2.setText("");
                    }
                    if(!matcher1.matches())
                    {
                        Toast.makeText(registeractivity.this, "oops!! InValid Email!!",Toast.LENGTH_LONG).show();
                        email1.setText("");
                    }
                }
                else {
                    //DBhelper db = new DBhelper(this);



                /*Toast.makeText(this, "error1", LENGTH_LONG).show();
                db.login_details(login, pass, email, phone, name);
                Intent i = new Intent(this,main.class);
                startActivity(i);
                Toast.makeText(this, "error", LENGTH_LONG).show();
                finish();*/
                    // call the encrypt function and pass the encrypted password
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(login, pass, name, email, Long.toString(phone));
                }
                break;
            default:
                break;
        }
    }


    private void updatePasswordStrengthView(String password) {

         strengthView = (TextView)findViewById(R.id.password_strength);


        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(MessageFormat.format(
                getText(R.string.password_strength_caption).toString(),
                str.getText(this)));
        strengthView.setTextColor(str.getColor());
    }


    class BackgroundTask extends AsyncTask<String,Void,Void> {
        String add_info_url;
        @Override
        protected  void onPreExecute()
        {
            add_info_url="http://routebuddy.comli.com/add_info.php";
        }

        @Override
        protected Void doInBackground(String... args) {
            String name,loginid,email,password;
            Long phone;
            loginid=args[0];
            password=args[1];
            name=args[2];
            email=args[3];
            phone=Long.parseLong(args[4]);
            String c=null;
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("loginid","UTF-8")+"="+URLEncoder.encode(loginid,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("phone","UTF-8")+"="+ URLEncoder.encode(Long.toString(phone),"UTF-8");
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
            Intent i=new Intent(registeractivity.this,loginpage.class);
            startActivity(i);
        }
    }
}

