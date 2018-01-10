package com.test.navigationdrawer1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.navigationdrawer1.Adapters.GenericSpinnerAdapter;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.CatEdoReporte;
import com.test.navigationdrawer1.REST.Models.ReporteIncidente;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormReportFragment extends Fragment {
    public static final int PICK_IMAGE = 1;

    String imageString;
    MainActivity activity;
    SharedPreferences pref;
    Button publish_report_btn, image_report_btn;
    EditText input_descripcion;
    ImageView image_report_imageview;

    public FormReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_report, container, false);

        activity = (MainActivity) getActivity();
        input_descripcion = (EditText) view.findViewById(R.id.input_descripcion);
        publish_report_btn = (Button) view.findViewById(R.id.publish_report_btn);
        image_report_imageview = (ImageView) view.findViewById(R.id.image_report_imageview);
        image_report_btn = (Button) view.findViewById(R.id.image_report_btn);

        publish_report_btn.setOnClickListener(btnPublishReport);
        image_report_btn.setOnClickListener(btnOpenGalleries);

        return view;
    }

    IHttpResponseMethods createReporte = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            Toast.makeText(activity.getApplicationContext(), jsonResponse,
                    Toast.LENGTH_LONG).show();
            Log.d("TEST", jsonResponse);

            Gson gson = new Gson();
            Type listType = new TypeToken<ReporteIncidente>(){}.getType();
            ReporteIncidente reporte_incidente = gson.fromJson(jsonResponse, listType);

            activity.api.responseMethods = uploadImage;
            activity.api.UploadImageReporteIncidente(imageString, reporte_incidente);
        }

        @Override
        public void error(String error) {
            Toast.makeText(activity.getApplicationContext(), error,
                    Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener btnPublishReport = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pref = activity.getSharedPreferences(getString(R.string.preference_user_key), MODE_PRIVATE);
            final String usrId = pref.getString(
                    getString(R.string.preference_user_id),
                    getString(R.string.default_user_id));

            final Date now = new Date();
            final String lat = String.valueOf(activity.location.getLatitude());
            final String lng = String.valueOf(activity.location.getLongitude());

            //TODO: validar imagenReporte
            ReporteIncidente test = new ReporteIncidente(){{
                this.fecha = new SimpleDateFormat(getString(R.string.format_date)).format(now);
                this.hora = new SimpleDateFormat(getString(R.string.format_hour)).format(now);
                this.descripcion = input_descripcion.getText().toString();
                this.imagenReporte = "";
                this.latitud = lat;
                this.longitud = lng;
                this.idUsuarioRI = Integer.parseInt(usrId);
                this.idEdoReporteRI = 4;
                this.idSismoRegistradoRI = 0;
            }};

            activity.api.responseMethods = createReporte;
            activity.api.CreateReporteIncidente(test);
        }
    };

    View.OnClickListener btnOpenGalleries = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            //Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.action_pick_image));
            //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Log.d("PICK_IMAGE", "" + data + "/");
            Uri imageUri = data.getData();
            image_report_imageview.setImageURI(imageUri);

            Log.e("IMAGE", "" + imageUri);
            if (imageUri != null) {
                //Get image
                try {
                    Bitmap reportPic = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);

                    imageString = getImageBase64String(reportPic);

                    Toast.makeText(getContext(), "Imagen pre-cargada", Toast.LENGTH_SHORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    IHttpResponseMethods uploadImage = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            /*Toast.makeText(activity.getApplicationContext(), jsonResponse,
                    Toast.LENGTH_LONG).show();*/

            MapFragment fragment = new MapFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        @Override
        public void error(String error) {
            /*Toast.makeText(activity.getApplicationContext(), error,
                    Toast.LENGTH_LONG).show();*/

            MapFragment fragment = new MapFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    };

    private String getImageBase64String(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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
