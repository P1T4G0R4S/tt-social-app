package com.test.navigationdrawer1.Utils;

import android.util.Log;

import com.test.navigationdrawer1.Network.DeviceType;

import java.util.HashMap;
import java.util.Map;

import edu.rit.se.wifibuddy.DnsSdService;

/**
 * Created by osvaldo on 10/31/17.
 */

public class NetworkUtil {
    private static NetworkUtil singleton;
    private DeviceType myType;
    private Map<String, DnsSdService> connectedDevices;

    private NetworkUtil() { }

    private NetworkUtil(DeviceType myType) {
        this.myType = myType;
    }

    public Map<String, DnsSdService> getConnectedDevices() {
        return connectedDevices;
    }

    public void setConnectedDevices(Map<String, DnsSdService> connectedDevices) {
        this.connectedDevices = connectedDevices;
    }

    public boolean canConnectTo(DnsSdService service) {
        return true;
    }

    public boolean canDiscoverTo(String discoveredDeviceType) {
        if(myType == DeviceType.EMITTER) {
            // ACCESS_POINT, because it has to send its information to a free ACCESS_POINT
            return discoveredDeviceType.equals(DeviceType.ACCESS_POINT.toString());
        }
        return false;
    }

    public static NetworkUtil getInstance(DeviceType deviceType) {
        if(singleton == null) {
            synchronized(NetworkUtil.class) {
                if(singleton == null) {
                    singleton = new NetworkUtil();
                    Log.d("DEBUG", "getInstance(): inside");
                }
            }
        }
        Log.d("DEBUG", "getInstance(): ");
        if(deviceType != null) {
            singleton.myType = deviceType;
        }

        if (singleton.connectedDevices == null) {
            singleton.connectedDevices = new HashMap<>();
        }

        return singleton;
    }
}
