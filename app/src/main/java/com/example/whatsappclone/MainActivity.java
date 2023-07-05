package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.Adapter.FragmentAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Hiding the actionbar
//        getSupportActionBar().hide();

        //Creating instance of firebase auth
        auth = FirebaseAuth.getInstance();

        //Syncing viewPager and tablayout with each other
        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Menu inflater is used to get menu XML into java object
        MenuInflater inflater = getMenuInflater();

        //inflate the menu hierarchy from the specified XML resource
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.logout: {
                auth.signOut(); //signing out

                //Removing email password to shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("myChattingApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userEmail", "");
                editor.putString("userPassword", "");
                editor.apply();

                //Go back to singin activity
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
            }
            case R.id.groupChat: {
                Intent intent = new Intent(this, GroupChatActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}