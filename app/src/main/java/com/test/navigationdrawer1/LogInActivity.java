package com.test.navigationdrawer1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.test.navigationdrawer1.REST.WebApi;

public class LogInActivity extends AppCompatActivity {
    WebApi api;
    AutoCompleteTextView email;
    EditText password;
    Button email_sign_in_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        api = new WebApi(this);

        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);

        email_sign_in_button.setOnClickListener(login);
    }

    View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavigateToMain();
        }
    };

    private void NavigateToMain() {
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
    }
}
