package com.example.myfirstapp.network;

public interface OnWebSocketMessageListener {

    /**
     * listener for when a message is received through the websocket
     * calls the DialogBuilderListener
     */
    void onReceivedMessage();

}
