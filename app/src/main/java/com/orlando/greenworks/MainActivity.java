package com.orlando.greenworks;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Year;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FirebaseHandler.OnUserCreatedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    static TextView fragmentTitle;
    private BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    static FirebaseAuth mAuth;
    FirebaseUser user;

    public static User currentUser;

    UIController uiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);

        NavigationView navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        TextView versionTV = navigationView.findViewById(R.id.version);
        TextView yearTV = navigationView.findViewById(R.id.copyRightYear);
        fragmentTitle = toolbar.findViewById(R.id.fragmentTitle);

        uiController = new UIController(MainActivity.this);

        MenuItem logoutItem = navigationView.getMenu().findItem(R.id.nav_logout);


        // Check if user is logged in
        if (user != null) {
            String uid = user.getUid();

            // Get user data from Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        currentUser = new User(
                                uid,
                                String.valueOf(dataSnapshot.child("firstName").getValue()),
                                String.valueOf(dataSnapshot.child("lastName").getValue()),
                                String.valueOf(dataSnapshot.child("address").getValue()),
                                user.getEmail(),
                                String.valueOf(dataSnapshot.child("phoneNumber").getValue())
                        );

                        currentUser.setSuite(String.valueOf(dataSnapshot.child("suite").getValue()));

                        if (logoutItem != null) {
                            logoutItem.setVisible(true);
                        }

                        uiController.changeFragment(new HomeFragment());

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error getting user data", databaseError.toException());
                }
            });
        } else {
           uiController.changeFragment(new WelcomeFragment());
            if (logoutItem != null) {
                logoutItem.setVisible(false);
            }
        }








        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



        // Set version name in the navigation drawer
        String versionName;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("MainActivity", "Version name not found");
            versionName = "N/A";
        }

        String version = getString(R.string.version) + versionName;
        versionTV.setText(version);

        // Set year in the navigation drawer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            yearTV.setText(String.valueOf(Year.now()));
        }



        // TODO: Disable placeholder item button

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            } else if (id == R.id.nav_map) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
            } else if (id == R.id.nav_calendar) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
            } else if (id == R.id.nav_history) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
            }

            return true;
        });

        fab.setOnClickListener(v -> {
            ScannerFragment scannerFragment = new ScannerFragment();
            scannerFragment.show(getSupportFragmentManager(), scannerFragment.getTag());
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            SettingFragment settingFragment = new SettingFragment();
            settingFragment.show(getSupportFragmentManager(), settingFragment.getTag());
        } else if (id == R.id.nav_terms) {
            TermsFragment termsFragment = new TermsFragment();
            termsFragment.show(getSupportFragmentManager(), termsFragment.getTag());

        } else if (id == R.id.nav_contact) {
            ContactFragment contactFragment = new ContactFragment();
            contactFragment.show(getSupportFragmentManager(), contactFragment.getTag());
        } else if (id == R.id.nav_logout) {
            confirmLogoutDialog();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void confirmLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("No", (dialog, which) -> {
                    // When user click on NO
                    dialog.cancel();
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    // When user click on YES
                    mAuth.signOut(); // Sign out from Server

                    currentUser = null;

                    Toast.makeText(this, getResources().getString(R.string.logout) + "!", Toast.LENGTH_SHORT).show();

                    recreate(); // Refresh MainActivity

                }).show();
    }


    protected void enableNavigationViews(int visibility) {
        toolbar.setVisibility(visibility);
        bottomAppBar.setVisibility(visibility);
        fab.setVisibility(visibility);
    }


    @Override
    public void onUserCreated() {
        recreate();
    }

}