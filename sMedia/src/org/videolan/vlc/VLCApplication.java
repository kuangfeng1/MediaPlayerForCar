/*****************************************************************************
 * VLCApplication.java
 *****************************************************************************
 * Copyright © 2010-2013 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/
package org.videolan.vlc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.videolan.vlc.util.AudioUtil;
import org.videolan.vlc.util.BitmapCache;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

public class VLCApplication extends Application {
    public final static String TAG = "VLC/VLCApplication";
    private static VLCApplication instance;
    private static VLCApplication inst;
    public final static String SLEEP_INTENT = "org.videolan.vlc.SleepIntent";
    /**�򿪵�activity**/
    private List<Activity> activities = new ArrayList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();

        // Are we using advanced debugging - locale?
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String p = pref.getString("set_locale", "");
        if (p != null && !p.equals("")) {
            Locale locale;
            // workaround due to region code
            if(p.equals("zh-TW")) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if(p.startsWith("zh")) {
                locale = Locale.CHINA;
            } else if(p.equals("pt-BR")) {
                locale = new Locale("pt", "BR");
            } else if(p.equals("bn-IN") || p.startsWith("bn")) {
                locale = new Locale("bn", "IN");
            } else {
                /**
                 * Avoid a crash of
                 * java.lang.AssertionError: couldn't initialize LocaleData for locale
                 * if the user enters nonsensical region codes.
                 */
                if(p.contains("-"))
                    p = p.substring(0, p.indexOf('-'));
                locale = new Locale(p);
            }
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }

        instance = this;

        // Initialize the database soon enough to avoid any race condition and crash
        MediaDatabase.getInstance();
        // Prepare cache folder constants
        AudioUtil.prepareCacheFolder(this);
    }

    /**
     * Called when the overall system is running low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "System is running low on memory");

        BitmapCache.getInstance().clear();
    }
    
    public static VLCApplication getInst(){
    	if(inst ==null){
    		inst =new VLCApplication();
    	}
    	return inst;
    }
    
    /**
     * @return the main context of the Application
     */
    public static Context getAppContext()
    {
        return instance;
    }

    /**
     * @return the main resources from the Application
     */
    public static Resources getAppResources()
    {
        if(instance == null) return null;
        return instance.getResources();
    }
    
    
    /**
     * �½���һ��activity
     * @param activity
     */
    public void addActivity(Activity activity){
        activities.add(activity);
    }
    /**
     *  ����ָ����Activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (activity!=null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * Ӧ���˳����������е�activity
     */
    public void exit(){
        for (Activity activity : activities) {
            if (activity!=null) {
                activity.finish();
            }
        }
        System.exit(0);
    }
    /**
     * �ر�Activity�б��е�����Activity*/
    public void finishActivity(){
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
            }
        }
        //ɱ����Ӧ�ý���  
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
