package com.example.heychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class usersAdapter extends RecyclerView.Adapter <usersAdapter.usersHolder> {

    private ArrayList<User> users;
    private Context context;
    private onUserClickListener onUserClickListener;

    public usersAdapter(ArrayList<User> users, Context context, usersAdapter.onUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    interface onUserClickListener{
        void onUserClicked(int position);
    }

    @NonNull
    @Override
    public usersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_holder,parent,false);
        return new usersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usersHolder holder, int position) {
        holder.txtUserName.setText(users.get(position).getUserName());
        Glide.with(context).load(users.get(position).getProfilePicture()).placeholder(R.drawable.account_img).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class usersHolder extends RecyclerView.ViewHolder{

        TextView txtUserName;
        ImageView imageView;

        public usersHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onUserClicked(getAdapterPosition());
                }
            });
            txtUserName = itemView.findViewById(R.id.txtUserName);
            imageView = itemView.findViewById(R.id.img_prof);



        }
    }
}

