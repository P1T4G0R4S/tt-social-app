package com.test.navigationdrawer1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.Usuario;
import com.test.navigationdrawer1.REST.WebApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity {
    WebApi api;
    SharedPreferences pref;
    EditText email, password;
    Button email_sign_in_button, email_sign_up_button;
    List<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        checkUserSharedPreferences();

        api = new WebApi(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        email_sign_up_button = (Button) findViewById(R.id.email_sign_up_button);

        email_sign_in_button.setOnClickListener(login);
        email_sign_up_button.setOnClickListener(signup);
    }

    private void checkUserSharedPreferences() {
        pref = LogInActivity.this.getSharedPreferences(
            getString(R.string.preference_user_key), MODE_PRIVATE);

        String emailShP = pref.getString(
                getString(R.string.preference_user_email),
                getString(R.string.default_user_email));
        if (!emailShP.equals("")) {
            navigateToMain();
        }
    }

    View.OnClickListener signup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navigateToSignUp();
        }
    };

    View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Usuario usr = new Usuario();
            usr.correoElectronico = email.getText().toString();
            usr.contrasena = password.getText().toString();
            api.responseMethods = queryUsuario;
            api.QueryUsuario(usr);
        }
    };

    private void navigateToMain() {
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
    }

    private void navigateToSignUp() {
        startActivity(new Intent(LogInActivity.this, SignupActivity.class));
    }

    IHttpResponseMethods queryUsuario = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            Log.e("queryUsuario", jsonResponse);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Usuario>>(){}.getType();
            usuarios = gson.fromJson(jsonResponse, listType);

            if (usuarios.size() > 0) {
                navigateToMain();
                savePreferences();
            } else {
                Snackbar.make(email_sign_up_button, getString(R.string.error_wrong_credentials),
                    Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void error(String error) {
            Log.e("queryUsuario", error);
        }
    };

    private void savePreferences() {
        Usuario usr = usuarios.get(0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.preference_user_email), email.getText().toString());
        editor.putString(getString(R.string.preference_user_name), usr.nombre);
        editor.putString(getString(R.string.preference_user_id), usr.id);
        editor.apply();
    }
}
