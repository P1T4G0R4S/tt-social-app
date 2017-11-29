package com.test.navigationdrawer1.Network;

/**
 * Created by osvaldo on 11/16/17.
 */

public enum ObjectType {
    REQUEST,    //Querier, Access point, Range extender devices to send a request
    RESPONSE,   //Querier, Access point, Range extender devices to send a response
    DISCOVERY,  //Range extender devices to ask for requests or responses
    HELLO,      //Emitter devices to send its own information
    RESULT,     //Temporary message that
    WAIT,
    POLL,
    OK,         //All devices to notify reception of sent message (if needed)
    ACK         //Notify the reception of sent message
}
