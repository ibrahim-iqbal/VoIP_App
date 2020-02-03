package com.example.voipapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    String email;


    PagerAdapter(Context context, FragmentManager fm,String email) {
        super(fm);
        mContext = context;
        this.email=email;
    }

    // This determines the fragment for each tab
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CallFragment(mContext);
        } else if (position == 1) {
            return new ChatFragment();
        } else {
            return new ProfileFragment(mContext,email);
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.call);
            case 1:
                return mContext.getString(R.string.chat);
            case 2:
                return mContext.getString(R.string.profile);
            default:
                return null;
        }
    }

}
