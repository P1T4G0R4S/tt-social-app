package com.test.navigationdrawer1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {
    MainActivity activity;
    SharedPreferences pref;
    TextView textview_user_name, textview_user_email;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        activity = (MainActivity) getActivity();

        final Switch switch_status = (Switch) view.findViewById(R.id.switch1);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
        textview_user_name = (TextView) view.findViewById(R.id.textview_user_name);
        textview_user_email = (TextView) view.findViewById(R.id.textview_user_email);

        pref = activity.getSharedPreferences(
                getString(R.string.preference_user_key), MODE_PRIVATE);

        String emailShP = pref.getString(
                getString(R.string.preference_user_email),
                getString(R.string.default_user_email));
        String nameShP = pref.getString(
                getString(R.string.preference_user_name),
                getString(R.string.default_user_name));

        textview_user_email.setText(String.format(getString(R.string.label_user_email), emailShP));
        textview_user_name.setText(String.format(getString(R.string.label_user_name), nameShP));

        switch_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pref = activity.getSharedPreferences(getString(R.string.preference_user_key), MODE_PRIVATE);
                final String userId = pref.getString(
                        getString(R.string.preference_user_id),
                        getString(R.string.default_user_id));

                if (b) {
                    activity.api.responseMethods = createHistorialEstadoUsuario;
                    activity.api.CreateHistorialEstadoUsuarios(new HistorialEstadoUsuario(){{
                        this.id = Integer.parseInt(userId);
                        this.idEventoHue = 1; //Constants //TODO: write enum
                        this.idEdoUsrHue = 1; //Constants //TODO: write enum
                    }});
                    imageView.setImageResource(R.mipmap.ic_secure);
                    switch_status.setClickable(false);
                    switch_status.setEnabled(false);
                } else {
                    activity.api.responseMethods = createHistorialEstadoUsuario;
                    activity.api.CreateHistorialEstadoUsuarios(new HistorialEstadoUsuario(){{
                        this.id = Integer.parseInt(userId);
                        this.idEventoHue = 2; //Constants //TODO: write enum
                        this.idEdoUsrHue = 2; //Constants //TODO: write enum
                    }});
                    imageView.setImageResource(R.mipmap.ic_unknown);
                }
            }
        });

        return view;
    }

    IHttpResponseMethods createHistorialEstadoUsuario = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            Toast.makeText(activity.getApplicationContext(), jsonResponse,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void error(String error) {
            Toast.makeText(activity.getApplicationContext(), error,
                    Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
