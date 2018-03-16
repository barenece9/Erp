package com.lnsel.erp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnsel.erp.MainActivity;
import com.lnsel.erp.R;
import com.lnsel.erp.constant.Sharepreferences;
import com.lnsel.erp.fragment.AttandanceFragment;
import com.lnsel.erp.fragment.HolidayFragment;
import com.lnsel.erp.fragment.HomeFragment;
import com.lnsel.erp.fragment.LeaveDetailsFragment;
import com.lnsel.erp.fragment.LeaveFragment;
import com.lnsel.erp.fragment.LeaveReviewFragment;
import com.lnsel.erp.fragment.LoanFragment;
import com.lnsel.erp.fragment.NoticeBordFragment;
import com.lnsel.erp.fragment.PayslipFragment;
import com.lnsel.erp.other.Logout;
import com.lnsel.erp.settergetter.UserInfo;

public class HomeScreen_2 extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "http://erp.lnsel.net/images/logo.png";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home_1";
    private static final String TAG_HOLIDAY = "holiday";
    private static final String TAG_ATTENDANCE = "attendance";
    private static final String TAG_LEAVE_APPLICATION = "leaveapplication";
    private static final String TAG_REVIEW_LEAVE_APPLICATION = "reviewleaveapplication";

    private static final String TAG_LOAN = "loan";
    private static final String TAG_PAYSLIP = "payslip";
    private static final String TAG_NOTICE = "notice";
    private static final String TAG_LEAVE_DETAILS = "leavedetails";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home_1 fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   toolbar.setNavigationIcon(R.drawable.holiday_icon);
        tv_title=(TextView)findViewById(R.id.tv_title);
       // tv_title.setText("Payroll");

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.holiday);
       // getSupportActionBar().setNavigationIcon(R.drawable.holiday);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles_2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeScreen_2.this, ApplyLeaveActivity.class));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }


    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        UserInfo rememberData= Sharepreferences.getUserinfo(HomeScreen_2.this);
        txtName.setText(rememberData.getUserName());
        txtWebsite.setText("www.lnsel.com");

        /*// loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);*/

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                HolidayFragment holidayFragment = new HolidayFragment();
                return holidayFragment;
            case 2:
                AttandanceFragment attandanceFragment = new AttandanceFragment();
                return attandanceFragment;
            case 3:
                LeaveFragment leaveFragment = new LeaveFragment();
                return leaveFragment;

           /* case 4:
                LeaveReviewFragment leaveReviewFragment = new LeaveReviewFragment();
                return leaveReviewFragment;*/

            case 4:
                LoanFragment loanFragment = new LoanFragment();
                return loanFragment;
            case 5:
                PayslipFragment payslipFragment = new PayslipFragment();
                return payslipFragment;
            case 6:
                NoticeBordFragment noticeFragment = new NoticeBordFragment();
                return noticeFragment;

            case 7:
                LeaveDetailsFragment leaveDetailsFragment = new LeaveDetailsFragment();
                return leaveDetailsFragment;
            default:
                return new HomeFragment();
        }
    }

    public void setToolbarTitle() {
       // getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        tv_title.setText(activityTitles[navItemIndex]);
    }

    public  void setActionbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_holiday:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_HOLIDAY;
                        break;
                    case R.id.nav_attendance:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ATTENDANCE;
                        break;
                    case R.id.nav_leave_app:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_LEAVE_APPLICATION;
                        break;
                   /* case R.id.nav_rev_leave_app:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_REVIEW_LEAVE_APPLICATION;
                        break;*/
                    case R.id.nav_loan_app:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_LOAN;
                        break;
                    case R.id.nav_payslip:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_PAYSLIP;
                        break;
                    case R.id.nav_notice:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_NOTICE;
                        break;
                    case R.id.nav_leave_details:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_LEAVE_DETAILS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(HomeScreen_2.this, MainActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(HomeScreen_2.this, ChangePasswordActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home_1 fragment when back key is pressed
        // when user is in other fragment than home_1
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home_1
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home_1 fragment is selected
       /* if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }*/

        // when fragment is notifications, load the menu created for notifications
       /* if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }*/
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Logout.logout(HomeScreen_2.this);
            startActivity(new Intent(HomeScreen_2.this,LoginScreen.class));
            finish();
            Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else if(navItemIndex == 3){
            fab.show();
        }
        else
            fab.hide();
    }
}
