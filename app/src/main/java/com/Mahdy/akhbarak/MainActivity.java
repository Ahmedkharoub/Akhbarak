package com.Mahdy.akhbarak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.Mahdy.akhbarak.Fragment.MainFragment;
import com.Mahdy.akhbarak.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_STACK = "MAIN_STACK";
    public static final String FRAGMENT_TAG = "MainActivity_Fragment_Tag";
    private Drawable drawable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean language = defaultSharedPreferences.getBoolean("Language", true);
        if (language){

            setLanguage("en");
        }else {
            setLanguage("ar");
        }
        System.out.println("Language is : " + language);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, MainFragment.newInstance())
                .addToBackStack(MAIN_STACK)
                .commitAllowingStateLoss();
    }
    public void setLanguage (String language){
        Locale newLocale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics db = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = newLocale;
        resources.updateConfiguration(configuration,db);
        onConfigurationChanged(configuration);
        Locale.setDefault(newLocale);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount > 0) { // we have opened fragments
            supportFragmentManager.popBackStack(MAIN_STACK, 0);
        } else { // we are closing the application
            finishAffinity();
        }
//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.setting_menu_xml, menu);
        drawable = menu.getItem(0).getIcon(); // set 0 if you have only one item in menu
        //this also will work
        //drawable = menu.findItem(your item id).getIcon();
        //
        if (drawable != null) {
            drawable.mutate();
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.setting_menu) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .addToBackStack(MAIN_STACK)
                    .commitAllowingStateLoss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        boolean isShow = false;
//        int scrollRange = -1;
//        if (scrollRange == -1) {
//            scrollRange = appBarLayout.getTotalScrollRange();
//        }
//        if (scrollRange + verticalOffset == 0) {
//            //collapse map
//            //TODO: change share icon color - set white share icon
//            isShow = true;
//            drawable.setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
//        } else if (isShow) {
//            //expanded map
//            //TODO: change share icon color - set dark share icon
//            isShow = false;
//            drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
//        }
//    }


    @Override
    protected void onResume() {
        boolean nightMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("DarkMode", false);
        if (nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onResume();
    }
}
