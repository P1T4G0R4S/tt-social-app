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

public class SignupActivity extends AppCompatActivity {
    WebApi api;
    SharedPreferences pref;
    EditText email_signup, password_signup, name_signup;
    Button email_sign_up_button;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        checkUserSharedPreferences();

        api = new WebApi(this);

        email_signup = (EditText) findViewById(R.id.email_signup);
        password_signup = (EditText) findViewById(R.id.password_signup);
        name_signup = (EditText) findViewById(R.id.name_signup);
        email_sign_up_button = (Button) findViewById(R.id.email_sign_up_button);

        email_sign_up_button.setOnClickListener(signup);

    }

    private void checkUserSharedPreferences() {
        pref = SignupActivity.this.getSharedPreferences(
                getString(R.string.preference_user_key), MODE_PRIVATE);

        String emailShP = pref.getString(
                getString(R.string.preference_user_email),
                getString(R.string.default_user_email));
        if (!emailShP.equals(getString(R.string.default_user_email))) {
            navigateToMain();
        }
    }

    View.OnClickListener signup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Usuario usr = new Usuario();

            usr.correoElectronico = email_signup.getText().toString();
            usr.contrasena = password_signup.getText().toString();
            usr.nombre = name_signup.getText().toString();

            api.responseMethods = createUsuario;
            api.CreateUsuario(usr);
        }
    };

    IHttpResponseMethods createUsuario = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            Log.e("createUsuario", jsonResponse);

            Gson gson = new Gson();
            Type listType = new TypeToken<Usuario>(){}.getType();
            usuario = gson.fromJson(jsonResponse, listType);

            if (usuario != null) {
                navigateToMain();
                savePreferences();
            } else {
                Snackbar.make(email_sign_up_button, getString(R.string.error_user_already_exists),
                        Snackbar.LENGTH_LONG).show();
            }

        }

        @Override
        public void error(String error) {
            Log.e("createUsuario", error);
        }
    };

    private void savePreferences() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.preference_user_email), email_signup.getText().toString());
        editor.putString(getString(R.string.preference_user_name), name_signup.getText().toString());
        editor.apply();
    }

    private void navigateToMain() {
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }
}
