package com.pushpal.talkie.view.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager mFragmentManager;
    ActionBarDrawerToggle mToggle;
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpActionBar(mBinding.toolbar);
        setUpNavigationDrawer();

        mFragmentManager = getSupportFragmentManager();
    }

    private void setUpNavigationDrawer() {
        mToggle = new ActionBarDrawerToggle(
                this,
                mBinding.drawerLayout,
                mBinding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(mToggle);
        mBinding.navView.setNavigationItemSelectedListener(this);
        mToggle.syncState();

        TextView nameTv = mBinding.navView.getHeaderView(0).findViewById(R.id.tv_name);
        TextView emailTv = mBinding.navView.getHeaderView(0).findViewById(R.id.tv_email);
        nameTv.setText(getResources().getString(R.string.dummy_name));
        emailTv.setText(getResources().getString(R.string.dummy_email));
    }

    private void setUpActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_item_most_popular:
                setTitle(menuItem.getTitle());
                break;
            case R.id.nav_item_top_rated:
                setTitle(menuItem.getTitle());
                break;
            case R.id.nav_item_favourite:
                setTitle(menuItem.getTitle());
                break;
            default:
                setTitle(menuItem.getTitle());
        }
        if (fragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mBinding.drawerLayout.closeDrawers();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
