package com.android.echostrings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.android.echostrings.fragments.LearnPageFragment;
import com.android.echostrings.fragments.SelfPageFragment;
import com.android.echostrings.fragments.SharePageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
    private Fragment last_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 去除顶部状态栏
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        loadInitialFragment(); // Load the initial fragment on startup

        /**
         * test for the ChordLearnActivity
         */
        startActivity(new Intent(MainActivity.this,ChordLearnActivity.class));
    }
    void initView(){
        // Initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(last_fragment);
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.menu_teach) {
                selectedFragment = fragmentManager.findFragmentByTag("LearnPage");
                if (selectedFragment == null) {
                    selectedFragment = new LearnPageFragment();
                    fragmentTransaction.add(R.id.main_fragment_container, selectedFragment, "LearnPage");
                }
            } else if (item.getItemId() == R.id.menu_self) {
                selectedFragment = fragmentManager.findFragmentByTag("SelfPage");
                if (selectedFragment == null) {
                    selectedFragment = new SelfPageFragment();
                    fragmentTransaction.add(R.id.main_fragment_container, selectedFragment, "SelfPage");
                }
            } else if (item.getItemId() == R.id.menu_share) {
                selectedFragment = fragmentManager.findFragmentByTag("SharePage");
                if (selectedFragment == null) {
                    selectedFragment = new SharePageFragment();
                    fragmentTransaction.add(R.id.main_fragment_container, selectedFragment, "SharePage");
                }
            }
            last_fragment=selectedFragment;
            fragmentTransaction.show(selectedFragment);
            fragmentTransaction.commit();
            return true;
        });
    }
    private void loadInitialFragment() {
        // Load the initial fragment (home)
        this.fragmentManager = getSupportFragmentManager();
        this.fragmentTransaction = fragmentManager.beginTransaction();
        Fragment tmp=new LearnPageFragment();
        fragmentTransaction.add(R.id.main_fragment_container, tmp, "LearnPage");
        this.last_fragment=tmp;
        fragmentTransaction.show(tmp);
        this.fragmentTransaction.commit();
    }
}