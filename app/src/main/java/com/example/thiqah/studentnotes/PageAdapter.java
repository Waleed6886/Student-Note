package com.example.thiqah.studentnotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter{

    int numberOfTabs;

    public PageAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        numberOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TabNoteFragment tabNoteFragment = new TabNoteFragment();
                return tabNoteFragment;
            case 1:
                TabPhotoFragment tabPhotoFragment = new TabPhotoFragment();
                return tabPhotoFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
