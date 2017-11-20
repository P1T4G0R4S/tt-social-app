package com.test.navigationdrawer1.Network;

import java.io.Serializable;

/**
 * Created by osvaldo on 11/16/17.
 */

public class Message implements Serializable {

    public MessageType messageType;
    public byte[] message;
    public ObjectType objectType;

    public Message(MessageType messageType, byte[] message) {
        this.messageType = messageType;
        this.message = message;
    }
}
