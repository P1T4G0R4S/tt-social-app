package com.test.navigationdrawer1.ListTab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.navigationdrawer1.Adapters.ReportsAdapter;
import com.test.navigationdrawer1.R;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;
import com.test.navigationdrawer1.REST.Models.ReporteIncidente;
import com.test.navigationdrawer1.REST.WebApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsListFragment extends Fragment {
    private WebApi api;
    private List<ReporteIncidente> reporteIncidentes = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportsAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;

    public ReportsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reports_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.reports_list_swipe);

        mAdapter = new ReportsAdapter(reporteIncidentes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        api = new WebApi(getActivity());
        api.responseMethods = reportsListResponse;
        api.QueryAllReporteIncidente();

        swipeContainer.setOnRefreshListener(refresh);
        
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

    IHttpResponseMethods reportsListResponse = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            Log.w("TEST", jsonResponse);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ReporteIncidente>>(){}.getType();
            reporteIncidentes = gson.fromJson(jsonResponse, listType);

            mAdapter.clear();
            mAdapter.addAll(reporteIncidentes);
        }

        @Override
        public void error(String error) {

        }
    };

    SwipeRefreshLayout.OnRefreshListener refresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            api.responseMethods = reportsListResponse;
            api.QueryAllReporteIncidente();
            swipeContainer.setRefreshing(false);
        }
    };
}
