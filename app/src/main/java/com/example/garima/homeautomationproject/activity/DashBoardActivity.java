package com.example.garima.homeautomationproject.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.fragment.HomeFragment;

import static com.example.garima.homeautomationproject.constant.Constant.HOME_BUNDLE;
import static com.example.garima.homeautomationproject.constant.Constant.HOME_NAME;

public class DashBoardActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    LinearLayout mDrawerPane;
    private ImageView backIcon;
    Toolbar toolbar;
    String homeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backIcon = (ImageView) findViewById(R.id.backImageView);
        backIcon.setVisibility(View.GONE);
        TextView tv_toolbar = (TextView) toolbar.findViewById(R.id.toolbarTextView);
        if (savedInstanceState == null) {


            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new HomeFragment()).commit();
            String text = "<font color=#4E4E4E>Add</font> <font color=#3D6B99>Home</font>";

            tv_toolbar.setText(Html.fromHtml(text));

        }
        navigationdrawer();


    }

    private void navigationdrawer() {
        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility


        );

        mDrawerToggle.setDrawerIndicatorEnabled(false);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menuicon, getApplicationContext().getTheme());

        mDrawerToggle.setHomeAsUpIndicator(drawable);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

}
