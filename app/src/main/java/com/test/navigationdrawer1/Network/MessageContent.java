package com.test.navigationdrawer1.Network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osvaldo on 11/16/17.
 */

public class MessageContent {
    @SerializedName("Label")
    String label;
    @SerializedName("Mac")
    String mac;
    @SerializedName("UserName")
    String userName;

    public MessageContent() {}

    public MessageContent(String label, String mac, String userName) {
        this.label = label;
        this.mac = mac;
        this.userName = userName;
    }
}
