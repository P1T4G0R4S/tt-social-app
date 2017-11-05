package com.test.navigationdrawer1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.test.navigationdrawer1.Network.DeviceType;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    MainActivity activity;
    SharedPreferences pref;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        pref = activity.getSharedPreferences("Options", MODE_PRIVATE);
        int deviceTypePref = pref.getInt("devicetype", DeviceType.EMITTER.getCode());
        DeviceType deviceType = DeviceType.get(deviceTypePref);
        Log.e("TEST", "" + deviceType);
        ImageView myImageView= (ImageView)activity.findViewById(R.id.broadcast);

        if (deviceType == DeviceType.ACCESS_POINT) {
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
            myImageView.startAnimation(myFadeInAnimation); //Set animation to your ImageView
            myImageView.setVisibility(View.VISIBLE);
        } else {
            myImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
