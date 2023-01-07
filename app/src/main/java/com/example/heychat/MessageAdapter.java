package com.example.heychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder>{

    private ArrayList<Message> messages;
    private String senderImg,receiverImg;
    private Context context;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseDatabase DB = FirebaseDatabase.getInstance("https://heychat-3cb5e-default-rtdb.firebaseio.com/");


    public MessageAdapter(ArrayList<Message> messages, String senderImg, String receiverImg, Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.txtMessage.setText(messages.get(position).getContent());

        ConstraintLayout constraintLayout = holder.ccLayout;

        if (messages.get(position).getSender().equals(Auth.getCurrentUser().getEmail())){
            Glide.with(context).load(senderImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profImg);
            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardView,constraintSet.LEFT);
            constraintSet.clear(R.id.chatMsg,constraintSet.LEFT);
            constraintSet.connect(R.id.profile_cardView,constraintSet.RIGHT,R.id.ccLayout,constraintSet.RIGHT, 0);
            constraintSet.connect(R.id.chatMsg,constraintSet.RIGHT,R.id.profile_cardView,constraintSet.LEFT, 0);
            constraintSet.applyTo(constraintLayout);
        }else{
            Glide.with(context).load(receiverImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profImg);
            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardView,constraintSet.RIGHT);
            constraintSet.clear(R.id.chatMsg,constraintSet.RIGHT);
            constraintSet.connect(R.id.profile_cardView,constraintSet.LEFT,R.id.ccLayout,constraintSet.LEFT, 0);
            constraintSet.connect(R.id.chatMsg,constraintSet.LEFT,R.id.profile_cardView,constraintSet.RIGHT, 0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{

        ConstraintLayout ccLayout;
        TextView txtMessage;
        ImageView profImg;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            ccLayout = itemView.findViewById(R.id.ccLayout);
            txtMessage = itemView.findViewById(R.id.chatMsg);
            profImg = itemView.findViewById(R.id.small_prof_img);
        }
    }
}

