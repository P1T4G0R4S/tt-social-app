package com.test.navigationdrawer1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.test.navigationdrawer1.Network.DeviceType;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoleFragment extends Fragment {

    MainActivity activity;
    DeviceType device;

    public RoleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role, container, false);
        RadioButton radioButton1 = (RadioButton)rootView.findViewById(R.id.radio_master);
        RadioButton radioButton2 = (RadioButton)rootView.findViewById(R.id.radio_slave);

        radioButton1.setOnClickListener(onRadioButtonClicked);
        radioButton2.setOnClickListener(onRadioButtonClicked);

        activity = (MainActivity) getActivity();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    View.OnClickListener onRadioButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((RadioButton) view).isChecked();

            switch(view.getId()) {
                case R.id.radio_slave:
                    if (checked)
                        device = DeviceType.EMITTER;
                    break;
                case R.id.radio_master:
                    if (checked)
                        device = DeviceType.ACCESS_POINT;
                    break;
            }

            SharedPreferences.Editor editor = activity.getSharedPreferences(getString(R.string.preference_device_key), MODE_PRIVATE).edit();
            editor.putInt(getString(R.string.preference_device_type), device.getCode());
            editor.apply();
        }
    };
}
