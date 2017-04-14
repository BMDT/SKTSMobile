package com.shsyarbin.skts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //deklarasi variabel
    EditText nama, email, konfirmasi, password;
    Button signup;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deklarasi memanggil id nya
        nama = (EditText) findViewById(R.id.registNama);
        email = (EditText) findViewById(R.id.registEmail);
        konfirmasi = (EditText) findViewById(R.id.registConfirm);
        password = (EditText) findViewById(R.id.registPassword);
        signup = (Button) findViewById(R.id.registSignUp);
        login= (TextView) findViewById(R.id.backlogin);

//aksi ketika tombol sign up di klik
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    // method untuk validasi
    private void Check() {
        nama.setError(null);
        email.setError(null);
        konfirmasi.setError(null);
        password.setError(null);

        boolean cancel = false;
        View focusView = null;

        //cek setiap editText
        if (Helper.isEmpty(nama)) {
            nama.setError("Nama tidak boleh kosong");
            cancel = true;
            focusView = nama;
        } else if (Helper.isEmpty(email)) {
            email.setError("Email tidak boleh kosong");
            cancel = true;
            focusView = email;
        } else if (!Helper.isEmailValid(email)) {
            email.setError("Email tidak valid");
            cancel = true;
            focusView = email;
        } else if (Helper.isEmpty(password)) {
            password.setError("Password tidak boleh kosong");
            cancel = true;
            focusView = password;
        } else if (Helper.isEmpty(konfirmasi)) {
            konfirmasi.setError("konfirmasi tidak boleh kosong");
            cancel = true;
            focusView = konfirmasi;
        } else if (Helper.isCompare(password, konfirmasi)) {
            konfirmasi.setError("konfirmasi tidak sama");
            cancel = true;
            focusView = konfirmasi;
        } else {
            Regristrasi();
        }
    }

    //method untuk parsing ke php
    private void Regristrasi() {
        String url = Helper.BASE_URL + "register.php";
        Map<String, String> params = new HashMap<>();
        params.put(Helper.NAMA_USER, nama.getText().toString());
        params.put(Helper.EMAIL_USER, email.getText().toString());
        params.put(Helper.PASSWORD_USER, password.getText().toString());

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading , , ,");
        progressDialog.setCancelable(true);

        AQuery aq;
        aq = new AQuery(MainActivity.this);

        aq.progress(progressDialog).ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                if (object != null) {
                    Helper.pre("Respon Login : "+ object);
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        String result = jsonObject.getString("success");
                        String pesan = jsonObject.getString("message");
                        if (result.equalsIgnoreCase("true")){
                            Helper.pesan(getApplicationContext(), pesan);
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }else {
                            Helper.pesan(getApplicationContext(), pesan);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                } else {
                    Helper.pesan(getApplicationContext(), "Url salah");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}