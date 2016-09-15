package nilay.tipme;


import android.content.Context;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;




import org.apache.http.Header;


public class Tabs extends AppCompatActivity {
private Toolbar mToolBar;
    private TabLayout tabLayout;
    private MyPagerAdapter myPagerAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        mToolBar = (Toolbar)findViewById(R.id.app_bar);
        mToolBar.setLogo(getResources().getDrawable(R.drawable.actionbarlogo));
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        setSupportActionBar(mToolBar);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager= (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(myPagerAdapter);

        tabLayout.setTabsFromPagerAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupIcons();
        tabLayout.setBackgroundColor(Color.argb(250,247,148,30));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
         mToolBar.setBackgroundColor(Color.argb(250,247,148,30));
        getSupportActionBar().setDisplayShowTitleEnabled(false);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setupIcons(){
        tabLayout.getTabAt(0).setIcon(R.drawable.paymenticon);
        tabLayout.getTabAt(1).setIcon(R.drawable.paymentsicon);
        tabLayout.getTabAt(2).setIcon(R.drawable.recepitsicon);
        tabLayout.getTabAt(3).setIcon(R.drawable.profileicon);

    }

}
class MyPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    public MyPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;




    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:

                fragment = new SelectUser();



                break;
            case 1:
                fragment = new ViewPayment();
                break;
            case 2:
                fragment = new ViewReceipts();
                break;
            case 3:
                fragment = new ViewProfile();



        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

   



    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
        return title;

    }

}



