package com.Mahdy.akhbarak.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.preference.PreferenceManager;

import com.Mahdy.akhbarak.Category;
import com.Mahdy.akhbarak.Fragment.NewsCategoryPageFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsCategoryPagerAdapter extends FragmentStatePagerAdapter {
    private List<Category> fragmentList = new ArrayList<>();
     Context context;

    public NewsCategoryPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;


    }



    public void setData(List<Category> data) {
        fragmentList = data;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
       return NewsCategoryPageFragment.newInstance(fragmentList.get(i).getId());

    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }


    public CharSequence getPageTitle(int position) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean language = defaultSharedPreferences.getBoolean("Language", true);
        Category category = fragmentList.get(position);
        if (language ) {
            return category.getName();
        } else {
            return category.getNameAr();
        }
  }
}
