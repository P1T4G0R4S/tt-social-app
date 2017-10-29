package com.test.navigationdrawer1.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.test.navigationdrawer1.MapTabs.PeopleMapFragment;
import com.test.navigationdrawer1.MapTabs.ReportsMapFragment;

/**
 * Created by osvaldo on 10/16/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PeopleMapFragment tab1 = new PeopleMapFragment();
                return tab1;
            case 1:
                ReportsMapFragment tab2 = new ReportsMapFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
