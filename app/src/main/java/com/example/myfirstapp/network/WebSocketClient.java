package com.example.myfirstapp.network;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myfirstapp.MainActivity;

import java.nio.channels.AcceptPendingException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketClient {

    private static WebSocket webSocket;
    private final SocketListener socketListener = new SocketListener();
    private DialogBuilderListener listener;


    public WebSocket getWebSocket() {
        if (webSocket == null) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(NetworkService.BASE_URL).build();
                webSocket = client.newWebSocket(request, socketListener);
                socketListener.setListener(new OnWebSocketMessageListener() {
                    @Override
                    public void onReceivedMessage() {
                        if (listener != null) {
                            listener.buildDialog();
                        }
                    }
                });
        }
        return webSocket;
    }

    public void setListener(DialogBuilderListener listener) {
        this.listener = listener;
    }
}
