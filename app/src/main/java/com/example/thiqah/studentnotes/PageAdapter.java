package com.example.thiqah.studentnotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    TabNoteFragment tabNoteFragment = new TabNoteFragment();
    TabPhotoFragment tabPhotoFragment = new TabPhotoFragment();
    TabDrawFragment tabDrawFragment = new TabDrawFragment();

    public PageAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        numberOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return tabNoteFragment;
            case 1:
                return tabPhotoFragment;
            case 2:
                return tabDrawFragment;
            default:
                return tabPhotoFragment;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
