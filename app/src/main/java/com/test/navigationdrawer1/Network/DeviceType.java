package com.test.navigationdrawer1.Network;

/*
* Posibles valores del tipo de dispositivo, los principales son:
 * RANGE_EXTENDER: intermediario
 * ACCESS_POINT: punto de acceso
 * QUERIER: buscador
 * EMITTER: emisor
* */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DeviceType {
    ACCESS_POINT(2),
    ACCESS_POINT_WREQ(5),
    ACCESS_POINT_WRES(6),
    EMITTER(4);

    private int id;

    DeviceType(int id) {
        this.id = id;
    }

    public int getCode() { return id; }

    public static DeviceType get(int id) {
        return lookup.get(id);
    }

    private static final Map<Integer,DeviceType> lookup
            = new HashMap<Integer,DeviceType>();

    static {
        for(DeviceType s : EnumSet.allOf(DeviceType.class))
            lookup.put(s.getCode(), s);
    }
}