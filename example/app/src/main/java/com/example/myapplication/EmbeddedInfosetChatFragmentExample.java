package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import app.infoset.android.InfosetChatErrorType;
import app.infoset.android.InfosetChatView;
import app.infoset.android.models.InfosetChatMessage;

public class EmbeddedInfosetChatFragmentExample extends Fragment implements InfosetChatView.ChatEventsListener,
        MainActivity.OnBackPressedListener {
    private static final String START_CHAT_TEXT = "Show chat";
    private Button startChatBtn;
    private Button reloadChatBtn;
    private InfosetChatView chatView;
    private int counter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_embedded_example, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startChatBtn = (Button) view.findViewById(R.id.embedded_start_chat);
        startChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatView.showChatWindow();
            }
        });
        reloadChatBtn = (Button) view.findViewById(R.id.embedded_reload_chat);
        reloadChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                chatView.reload();
            }
        });
        chatView = (InfosetChatView) view.findViewById(R.id.embedded_chat_window);
        chatView.setUpWindow(BaseApplication.getInfosetChatConfiguration());
        chatView.setUpListener(this);
        chatView.initialize();
    }


    @Override
    public void onChatWindowVisibilityChanged(boolean visible) {
        if (visible) {
            counter = 0;
            startChatBtn.setText(START_CHAT_TEXT);
        }
    }

    @Override
    public void onNewMessage(InfosetChatMessage message, boolean windowVisible) {
        if (!windowVisible) {
            counter++;
            startChatBtn.setText(START_CHAT_TEXT + " (" + counter + ")");
        }
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
        if (isAdded()) {
            if (errorType == InfosetChatErrorType.WebViewClient && errorCode == -2 && chatView.isChatLoaded()) {
                // chat view can handle reconnection, so you might want to delegate this
                return false;
            } else {
                reloadChatBtn.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getActivity(), errorDescription, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        chatView.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onBackPressed() {
        return chatView.onBackPressed();
    }

    public static Fragment newInstance() {
        return new EmbeddedInfosetChatFragmentExample();
    }
}
