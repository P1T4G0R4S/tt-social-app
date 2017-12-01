package com.test.navigationdrawer1.ListTab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.navigationdrawer1.Adapters.ReportsAdapter;
import com.test.navigationdrawer1.R;
import com.test.navigationdrawer1.REST.Models.ReporteIncidente;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsListFragment extends Fragment {
    private List<ReporteIncidente> reporteIncidentes = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportsAdapter mAdapter;

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

        mAdapter = new ReportsAdapter(reporteIncidentes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        
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
}
