package com.Mahdy.akhbarak;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);


        SwitchPreferenceCompat switchPreferenceCompat = findPreference("DarkMode");

        switchPreferenceCompat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    if ((Boolean) newValue) {
                        //these lines so that the preference persists
                        getPreferenceManager().getSharedPreferences().edit().putBoolean("DarkMode", true).apply();
                        //you do not need to finish and recreate activity
                        //it takes care of any such things that needs to be done
                        //automatically
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    } else {
                        getPreferenceManager().getSharedPreferences().edit().putBoolean("DarkMode", false).apply();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                }
                return true;
            }
        });
//        boolean mBoolean = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("NightMode", false);


        SwitchPreferenceCompat switchPreferenceLang = findPreference("Language");

        switchPreferenceLang.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    System.out.println(newValue);
                    if ((Boolean) newValue) {
                        System.out.println("en");
                        //these lines so that the preference persists
                        getPreferenceManager().getSharedPreferences().edit().putBoolean("Language", true).apply();
                        //you do not need to finish and recreate activity
                        //it takes care of any such things that needs to be done
                        //automatically
                        setLanguage("en");

                    } else {
                        System.out.println("ar");
                        getPreferenceManager().getSharedPreferences().edit().putBoolean("Language", false).apply();
                        setLanguage("ar");

                    }
                }
                return true;
            }
        });
//        boolean mBoolean = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("NightMode", false);
    }

public void setLanguage (String language){
    Locale newLocale = new Locale(language);
    Resources resources = getResources();
    DisplayMetrics db = resources.getDisplayMetrics();
    Configuration configuration = resources.getConfiguration();
    configuration.locale = newLocale;
    resources.updateConfiguration(configuration,db);
//    onConfigurationChanged(configuration);
    Locale.setDefault(newLocale);
//    triggerRebirth(getContext());
    restartApplication();
}

    private void restartApplication() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
        getActivity().finishAffinity();
    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }


}