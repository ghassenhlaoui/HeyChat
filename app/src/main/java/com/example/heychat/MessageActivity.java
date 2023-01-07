package com.example.heychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtChat;
    private TextView txtChatReceiver;
    private ImageView imgChat,imgSend;
    private ProgressBar chatProgressBar;
    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseDatabase DB = FirebaseDatabase.getInstance("https://heychat-3cb5e-default-rtdb.firebaseio.com/");


    String recieverUsername, recieverEmail,chatRoomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recieverUsername = getIntent().getStringExtra("username_receiver");
        recieverEmail = getIntent().getStringExtra("email_receiver");


        recyclerView = findViewById(R.id.recyclerChat);
        edtChat = findViewById(R.id.edtChat);
        txtChatReceiver = findViewById(R.id.txtChat);
        chatProgressBar = findViewById(R.id.progressChat);
        imgChat = findViewById(R.id.img_toolBar);
        imgSend = findViewById(R.id.imgSend);

        txtChatReceiver.setText(recieverUsername);

        messages = new ArrayList<>();
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.getReference("Messages/"+chatRoomId).push().setValue(new Message(Auth.getCurrentUser().getEmail(),recieverEmail,edtChat.getText().toString()));
                edtChat.setText("");
            }
        });
        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_image"),getIntent().getStringExtra("img_receiver"),MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_receiver")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imgChat);
        setUpChatID();



    }

    private void setUpChatID(){
        DB.getReference("User/"+Auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(User.class).getUserName();
                if(recieverUsername.compareTo(myUsername)>0){
                    chatRoomId = myUsername+recieverUsername;
                }else if(recieverUsername.compareTo(myUsername)==0){
                    chatRoomId= myUsername+recieverUsername;
                }else{
                    chatRoomId = recieverUsername+myUsername;
                }
                attachMessagesListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void attachMessagesListener(String chatRoomId){
        DB.getReference("Messages/"+chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                chatProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }}
