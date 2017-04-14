package com.shsyarbin.skts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                }
                catch (InterruptedException e ) {}
                finally {
                    Intent i = new Intent(Splashscreen.this, Login.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        splashThread.start();
    }
}
