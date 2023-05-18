package com.android.finalproject_admin.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.finalproject_admin.R;
import com.android.finalproject_admin.fragments.OrderFragment;
import com.android.finalproject_admin.fragments.ProductFragment;
import com.android.finalproject_admin.fragments.StatisticsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerlayout;
    ImageView menu;
    LinearLayout home, order, logout, statistic;
    Fragment homeFragment, orderFragment, statisticsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        homeFragment = new ProductFragment();
        loadFragment(homeFragment);

        //Get token
        /* FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast
                        Log.d("tokenmessaging", token);
                    }
                });

        //Dang ki nhan thong bao
        FirebaseMessaging.getInstance().subscribeToTopic("admin_app")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Subscribed to admin_app topic");
                        } else {
                            Log.w(TAG, "Subscription to admin_app topic failed", task.getException());
                        }
                    }
                }); */

    }

    private void initView() {
        drawerlayout = findViewById(R.id.drawerlayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.homePro);
        order = findViewById(R.id.order);
        statistic = findViewById(R.id.statistic);
        logout = findViewById(R.id.logout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerlayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment = new ProductFragment();
                loadFragment(homeFragment);
                closeDrawer(drawerlayout);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderFragment = new OrderFragment();
                loadFragment(orderFragment);
                closeDrawer(drawerlayout);
            }
        });
        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statisticsFragment = new StatisticsFragment();
                loadFragment(statisticsFragment);
                closeDrawer(drawerlayout);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });
    }

    private void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle("Warning: ");
        builder.setMessage("Are you sure? If you proceed you will be logged out immediately?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerlayout);
    }
}