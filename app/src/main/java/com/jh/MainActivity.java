package com.jh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.joehukum.chat.JoeHukum;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.ui.notification.NotificationHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.client_hash)
    EditText clientHash;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.phone_number)
    EditText phoneNumber;


    UserStore userStore = new UserStore();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User user = userStore.getUser(this);
        if (user != null)
        {
            clientHash.setText(user.getClientHash());
            email.setText(user.getEmail());
            phoneNumber.setText(user.getPhone());
            clientHash.setKeyListener(null);
            email.setKeyListener(null);
            phoneNumber.setKeyListener(null);
            JoeHukum.chat(this, user.getClientHash(), user.getPhone(), user.getEmail(), null);
        } else
        {

        }
    }

    @OnClick(R.id.btn)
    public void btnClick(View v)
    {
        String clHash = clientHash.getText().toString();
        if (TextUtils.isEmpty(clHash))
        {
            clientHash.setError("Enter client hash");
            return;
        }
        String em = email.getText().toString();
        String ph = phoneNumber.getText().toString();
        userStore.saveUser(this, new User(clHash, ph, em));
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("make", "Bajaj");
//        params.put("model", "V15");
//        params.put("year", "2016");
//        params.put("trim", "150cc");
//        params.put("dlid", "1415453326");
//        params.put("category", "Motorcycle/Bike");
//        JoeHukum.setUserParams(this, params);
        JoeHukum.chat(this, clHash, ph, em, null);
    }
}
