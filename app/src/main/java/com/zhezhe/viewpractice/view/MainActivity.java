package com.zhezhe.viewpractice.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhezhe.viewpractice.R;
import com.zhezhe.viewpractice.tiktok.Tiktok;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    drawerLayout.closeDrawers();
                }

                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.drawer_item_home:
                        fragment = ShotListFragment.newInstance();
                        setTitle(R.string.title_home);
                        break;
                    case R.id.drawer_item_likes:
                        fragment = ShotListFragment.newInstance();
                        setTitle(R.string.title_home);
                        break;
                    case R.id.drawer_item_buckets:
                        fragment = ShotListFragment.newInstance();
                        setTitle(R.string.title_home);
                        break;
                }

                drawerLayout.closeDrawers();

                if (Objects.nonNull(fragment)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
                return false;
            }
        });

        View  headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.nav_header_user_name)).setText(Tiktok.getCurrentUser().toString());
        ((SimpleDraweeView) headerView.findViewById(R.id.nav_header_user_picture))
                .setImageURI(Uri.parse(Tiktok.getCurrentUser().toString()));

        headerView.findViewById(R.id.nav_header_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tiktok.unStoreTokenAndUserByContextSharedPreferrence(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
