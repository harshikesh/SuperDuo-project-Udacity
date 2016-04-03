package com.example.harshikesh.footballscores.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.example.harshikesh.footballscores.R;
import com.example.harshikesh.footballscores.adapter.TabAdapter;
import com.example.harshikesh.footballscores.util.Utilities;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
  private static final int NUM_PAGES = 5;
  public static int selected_match_id;
  public static int current_fragment = 2;
  public static String LOG_TAG = "MainActivity";
  public static int selectedTabPosition = 0;
  private final String save_tag = "Save Test";
  //private PagerFragment my_main;
  private ViewPager mViewPager;
  private TabAdapter mAdapter;
  private TabLayout mTabLayout;
  private FrameLayout mNoConnLayout;
  //private PagerFragment my_main;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mViewPager = (ViewPager) findViewById(R.id.viewpager);
    mTabLayout = (TabLayout) findViewById(R.id.tabs);
    mNoConnLayout = (FrameLayout) findViewById(R.id.container);

    for (int pos = 0; pos < NUM_PAGES; pos++) {
      mTabLayout.addTab(mTabLayout.newTab().setText(Utilities.getPageTitle(this, pos)));
    }


    mAdapter = new TabAdapter(getSupportFragmentManager(), NUM_PAGES);
    mViewPager.setAdapter(mAdapter);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    mTabLayout.setOnTabSelectedListener(this);
    mViewPager.setCurrentItem(2);

    if (Utilities.isNetworkAvailable(getApplicationContext())) {
      mNoConnLayout.setVisibility(View.GONE);
    } else {
      mNoConnLayout.setVisibility(View.VISIBLE);
      Snackbar.make(mViewPager,R.string.noInternet,Snackbar.LENGTH_SHORT);
    }
    if (savedInstanceState == null) {
      //my_main = new PagerFragment();
    //  getSupportFragmentManager().beginTransaction().add(R.id.container, my_main).commit();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_about) {
      Intent start_about = new Intent(this, AboutActivity.class);
      startActivity(start_about);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putInt("Selected_adapter_position",selectedTabPosition);
    outState.putInt("Selected_match", selected_match_id);
    super.onSaveInstanceState(outState);
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    current_fragment = savedInstanceState.getInt("Pager_Current");
    selected_match_id = savedInstanceState.getInt("Selected_match");
    selectedTabPosition = savedInstanceState.getInt("Selected_adapter_position",0);
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override public void onTabSelected(TabLayout.Tab tab) {
    mViewPager.setCurrentItem(tab.getPosition());
    selected_match_id = tab.getPosition();
    mAdapter.notifyDataSetChanged();
  }

  @Override public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override public void onTabReselected(TabLayout.Tab tab) {

  }
}
