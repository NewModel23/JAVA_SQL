package com.linkcode.sql.mr2guer.ejemploconexionsql;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText edtuserid,edtpass;
    Button btnlogin;
    ProgressBar pbbar;
    public String Valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        connectionClass = new ConnectionClass(); //&nbsp;//the class file
        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin(); //&nbsp;// this is the Asynctask
                doLogin.execute("");
            }
        });


    }
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {

                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                i.putExtra("usuario", userid.toString());
                startActivity(i);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(userid.trim().equals("")|| password.trim().equals(""))
                z = "Please enter User Id and Password";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select TIPO_FALLA FROM DT_FALLOS_MANTENIMIENTO WHERE ID =2";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {
                            //Valor = rs.getString(0);
                            z = "Login successfull";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }

}

