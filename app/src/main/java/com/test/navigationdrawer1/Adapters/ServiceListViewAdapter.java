package com.test.navigationdrawer1.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.navigationdrawer1.MainActivity;
import com.test.navigationdrawer1.R;
import com.test.navigationdrawer1.Utils.NetworkUtil;

import java.util.List;
import java.util.Map;

import edu.rit.se.wifibuddy.DnsSdService;
import edu.rit.se.wifibuddy.DnsSdTxtRecord;

/**
 * Created by osvaldo on 10/31/17.
 */

public class ServiceListViewAdapter extends BaseAdapter {
    private List<DnsSdService> serviceList;
    private MainActivity context;
    NetworkUtil networkUtil;

    public ServiceListViewAdapter(MainActivity context, List<DnsSdService> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    public Boolean addUnique(DnsSdService service) {
        Map mapTxtRecord;
        String strTxtRecord = "";
        if (context.getWifiHandler() != null) {
            DnsSdTxtRecord txtRecord = context.getWifiHandler().getDnsSdTxtRecordMap().get(service.getSrcDevice().deviceAddress);
            if (txtRecord != null) {
                mapTxtRecord = txtRecord.getRecord();
                strTxtRecord = mapTxtRecord.get("DeviceType").toString();
            }
        }

        Log.d("Adapter", strTxtRecord);

        networkUtil = NetworkUtil.getInstance(null);
        if (!networkUtil.canDiscoverTo(strTxtRecord)) {
            return false;
        }

        if (serviceList.contains(service)) {
            int idxService = serviceList.indexOf(service);
            serviceList.set(idxService, service);
            this.notifyDataSetChanged();
            return false;
        } else {
            serviceList.add(service);
            this.notifyDataSetChanged();
            return true;
        }
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public DnsSdService getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DnsSdService service = getItem(position);

        // Inflates the template view inside each ListView item
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.service_item, parent, false);
        }

        TextView deviceNameTextView = (TextView) convertView.findViewById(R.id.deviceName);
        TextView deviceInfoTextView = (TextView) convertView.findViewById(R.id.deviceInfo);
        TextView connectTextView = (TextView) convertView.findViewById(R.id.connect);
        connectTextView.setText("Connect");

        String sourceDeviceName = service.getSrcDevice().deviceName;
        if (sourceDeviceName.equals("")) {
            sourceDeviceName = "Android Device";
        }
        deviceNameTextView.setText(sourceDeviceName);

        Map<String, String> mapTxtRecord;
        String strTxtRecord = "";
        if (context.getWifiHandler() != null) {
            DnsSdTxtRecord txtRecord = context.getWifiHandler().getDnsSdTxtRecordMap().get(service.getSrcDevice().deviceAddress);
            if (txtRecord != null) {
                mapTxtRecord = txtRecord.getRecord();
                for (Map.Entry<String, String> record : mapTxtRecord.entrySet()) {
                    strTxtRecord += record.getKey() + ": " + record.getValue() + "\n";
                }
            }
        }
        String status = context.getWifiHandler().deviceStatusToString(context.getWifiHandler().getThisDevice().status);
        String strDeviceInfo = status + "\n" + strTxtRecord;
        deviceInfoTextView.setText(strDeviceInfo);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.onServiceClick(service);
            }
        });

        return convertView;
    }
}
