package com.test.navigationdrawer1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {
    MainActivity activity;

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

        Switch switch_status = (Switch) view.findViewById(R.id.switch1);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);

        switch_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //TODO: colocar los valores correctos
                    activity.api.responseMethods = createHistorialEstadoUsuario;
                    activity.api.CreateHistorialEstadoUsuarios(new HistorialEstadoUsuario(){{
                        this.id = 3;
                        this.idEventoHue = 1;
                        this.idEdoUsrHue = 1;
                    }});
                    imageView.setImageResource(R.mipmap.ic_secure);
                } else {
                    //TODO: colocar los valores correctos
                    activity.api.responseMethods = createHistorialEstadoUsuario;
                    activity.api.CreateHistorialEstadoUsuarios(new HistorialEstadoUsuario(){{
                        this.id = 3;
                        this.idEventoHue = 2;
                        this.idEdoUsrHue = 2;
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
