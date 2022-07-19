package com.example.myfirstapp.network;

public interface DialogBuilderListener {

    /**
     * this listener is implemented in main activity
     * it builds an alert dialog
     * it is called from the onWebSocketMessageListener
     */
    void buildDialog();
}
