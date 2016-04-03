package com.example.harshikesh.footballscores.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.harshikesh.footballscores.ui.MainScreenFragment;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by harshikesh.kumar on 03/04/16.
 */
public class TabAdapter extends FragmentPagerAdapter {

  private final int mNumOfPAges;

  public TabAdapter(FragmentManager fm, int num) {
    super(fm);
    mNumOfPAges = num;
  }

  @Override public Fragment getItem(int position) {

    Date fragmentDate = new Date(System.currentTimeMillis() + ((position - 2) * 86400000));

    SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

    switch (position) {
      case 0:
        MainScreenFragment tab1 = new MainScreenFragment();
        tab1.setFragmentDate(mformat.format(fragmentDate),position);
        return tab1;
      case 1:
        MainScreenFragment tab2 = new MainScreenFragment();
        tab2.setFragmentDate(mformat.format(fragmentDate),position);
        return tab2;
      case 2:
        MainScreenFragment tab3 = new MainScreenFragment();
        tab3.setFragmentDate(mformat.format(fragmentDate),position);
        return tab3;

      case 3:
        MainScreenFragment tab4 = new MainScreenFragment();
        tab4.setFragmentDate(mformat.format(fragmentDate),position);
        return tab4;
      case 4:
        MainScreenFragment tab5 = new MainScreenFragment();
        tab5.setFragmentDate(mformat.format(fragmentDate),position);
        return tab5;
      default:
        return null;
    }
  }

  @Override public int getCount() {
    return mNumOfPAges;
  }

  @Override
  public int getItemPosition(Object object) {
    MainScreenFragment f = (MainScreenFragment ) object;
    if (f != null) {
      f.update();
    }
    return super.getItemPosition(object);
  }

}
