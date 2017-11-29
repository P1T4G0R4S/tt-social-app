package com.test.navigationdrawer1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.Usuario;
import com.test.navigationdrawer1.REST.WebApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

        String emailShP = pref.getString(getString(R.string.preference_user_saved), "");
        if (!emailShP.equals("")) {
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
            Toast.makeText(getApplicationContext(), jsonResponse,
                    Toast.LENGTH_LONG).show();

            Gson gson = new Gson();
            Type listType = new TypeToken<Usuario>(){}.getType();
            usuario = gson.fromJson(jsonResponse, listType);

            if (usuario != null) {
                navigateToMain();
                savePreferences();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.wrong_credentials),
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void error(String error) {
            Toast.makeText(getApplicationContext(), error,
                    Toast.LENGTH_LONG).show();
        }
    };

    private void savePreferences() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.preference_user_saved), email_signup.getText().toString());
        editor.putString(getString(R.string.preference_user_name_saved), name_signup.getText().toString());
        editor.apply();
    }

    private void navigateToMain() {
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }
}
