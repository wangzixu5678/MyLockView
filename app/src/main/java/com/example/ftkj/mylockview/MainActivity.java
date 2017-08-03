package com.example.ftkj.mylockview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private LockView mLockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLockView = (LockView) findViewById(R.id.ac_lockview);
    }

    public void getPassword(View view) {
        String password = mLockView.getPassword();
        Toast.makeText(this, ""+password, Toast.LENGTH_SHORT).show();
    }
}
