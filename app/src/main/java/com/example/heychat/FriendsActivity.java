package com.example.heychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    final FirebaseAuth Auth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private usersAdapter usersAdapter;
    private FirebaseDatabase DB = FirebaseDatabase.getInstance("https://heychat-3cb5e-default-rtdb.firebaseio.com/");
    private SwipeRefreshLayout swipeRefreshLayout;

    String myImgUrl;

    usersAdapter.onUserClickListener onUserClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        swipeRefreshLayout=findViewById(R.id.swipeLayout);
        progressBar = findViewById(R.id.progressBar);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        onUserClickListener = new usersAdapter.onUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                startActivity( new Intent(FriendsActivity.this, MessageActivity.class)
                        .putExtra("username_receiver",users.get(position).getUserName())
                        .putExtra("email_receiver",users.get(position).getEmail())
                        .putExtra(
                                "img_receiver",users.get(position).getProfilePicture())
                        .putExtra("my_image",myImgUrl)
                );
            }
        };
        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_profile) {
            startActivity(new Intent(FriendsActivity.this, Profile.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUsers(){
        users.clear();
        DB.getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    users.add(dataSnapshot.getValue(User.class));
                }
                usersAdapter = new usersAdapter(users,FriendsActivity.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
                recyclerView.setAdapter(usersAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                for (User user: users){
                    if (user.getEmail().equals(Auth.getCurrentUser().getEmail())){
                        myImgUrl = user.getProfilePicture();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}