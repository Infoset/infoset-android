package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.infoset.android.InfosetChatErrorType;
import app.infoset.android.InfosetChatView;
import app.infoset.android.models.InfosetChatMessage;

import static android.view.View.GONE;

public class FullScreenWindowActivityExample extends AppCompatActivity implements InfosetChatView.ChatEventsListener {
    private FloatingActionButton startChatBtn;
    private InfosetChatView chatView;
    private TextView chatBadgeTv;
    private int badgeCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_window_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chatView = InfosetChatView.createAndAttachChatWindowInstance(FullScreenWindowActivityExample.this);
        chatView.setUpWindow(BaseApplication.getInfosetChatConfiguration());
        chatView.setUpListener(this);
        chatView.initialize();
        startChatBtn = (FloatingActionButton) findViewById(R.id.start_chat);
        startChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChatWindow();
            }
        });
        chatBadgeTv = (TextView) findViewById(R.id.chat_badge);
    }

    private void showChatWindow() {
        chatView.showChatWindow();
    }

    @Override
    public void onChatWindowVisibilityChanged(boolean visible) {
        if (visible) {
            discardBadge();
        }
    }

    private void discardBadge() {
        badgeCounter = 0;
        chatBadgeTv.setVisibility(GONE);
        chatBadgeTv.setText("");
    }

    @Override
    public void onNewMessage(InfosetChatMessage message, boolean windowVisible) {
        if (!windowVisible) {
            badgeCounter++;
            chatBadgeTv.setVisibility(View.VISIBLE);
            chatBadgeTv.setText(String.valueOf(badgeCounter));
        }
    }

    @Override
    public void onRoomOpened(long roomId) {
        Toast.makeText(FullScreenWindowActivityExample.this, "onRoomOpened " + roomId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoomClosed(long roomId) {
        Toast.makeText(FullScreenWindowActivityExample.this, "onRoomClosed " + roomId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoomReopened(long roomId) {
        Toast.makeText(FullScreenWindowActivityExample.this, "onRoomReopened " + roomId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean handleUri(Uri uri) {
        return false;
    }

    @Override
    public void onStartFilePickerActivity(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onError(InfosetChatErrorType errorType, int errorCode, String errorDescription) {
        Toast.makeText(FullScreenWindowActivityExample.this, errorDescription, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!chatView.onBackPressed())
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatView.onActivityResult(requestCode, resultCode, data);
    }
}
