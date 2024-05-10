package com.nguyenthithao.thestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.android.material.textfield.TextInputEditText;
import com.nguyenthithao.model.ResponseCallback;
import com.nguyenthithao.model.TheStoreGemini;
import com.nguyenthithao.thestore.databinding.ActivityChatbotBinding;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ChatbotActivity extends AppCompatActivity {
    ActivityChatbotBinding binding;
    private ChatFutures chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chatbot);
        binding = ActivityChatbotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayActionBar();
        chatModel = getChatModel();

        binding.btnSendMessage.setOnClickListener(v -> {
            String query = binding.edtMessage.getText().toString();
            binding.sendMessageProgressBar.setVisibility(View.VISIBLE);

            binding.edtMessage.setText("");
            populateChatBody("You", query, getDate());

            TheStoreGemini.getResponse(chatModel, query, new ResponseCallback() {
                @Override
                public void onResponse(String response) {
                    binding.sendMessageProgressBar.setVisibility(View.GONE);
                    populateChatBody("GeminiPro", response, getDate());
                }

                @Override
                public void onError(Throwable throwable) {
                    populateChatBody("GeminiPro", "Sorry, I'm having trouble understanding that. Please try again.", getDate());
                    binding.sendMessageProgressBar.setVisibility(View.GONE);
                }
            });
        });
    }

    private ChatFutures getChatModel() {
        TheStoreGemini model = new TheStoreGemini();
        GenerativeModelFutures modelFutures = model.getModel();

        return modelFutures.startChat();
    }

    public void populateChatBody(String userName, String message, String date) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_chat, null);

        TextView userAgentName = view.findViewById(R.id.txtUserAgentName);
        TextView userAgentMessage = view.findViewById(R.id.txtUserAgentMessage);
        TextView dateTextView = view.findViewById(R.id.txtMessageDate);

        userAgentName.setText(userName);
        userAgentMessage.setText(message);
        dateTextView.setText(date);

        binding.chatResponseLayout.addView(view);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private String getDate() {
        Instant instant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm").withZone(ZoneId.systemDefault());

        return formatter.format(instant);
    }

    private void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        actionBar.setTitle(Html.fromHtml("<font color='#5C3507'>The Store Chat bot</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}