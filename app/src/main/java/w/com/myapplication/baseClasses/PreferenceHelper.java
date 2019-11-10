package w.com.myapplication.baseClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

public class PreferenceHelper {

    // Shared preferences file name
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String KEY = "getKey";
    public static final String USER = "user";
    public static final String USER_ID = "userId";
    public static final String IS_PHONE_VERIFIED = "isPhoneVerified";
    public static final String PROFILE = "Profile";
    public static final String PHONE_NUMBER = "PhoneNumber";
    public static final String PHONE_CODE = "PhoneCode";
    public static final String DEVICE_TOKEN = "Device_token";
    public static final String IS_REGISTERED = "isRegistered";
    public static final String TEAMS = "Teams";
    private static SharedPreferences pref;
    private static PreferenceHelper prefHelper;
    public static String ACCESS_TOKEN = "access_token";
    public static String IS_LOGGED_IN = "isLoggedIn";
    public static String NOTIFICATION_STATUS = "notification-status";
    public static String IS_PROFILE_CREATED = "isProfileCreated";
    public static String IS_MOBILE_VERIFIED = "mobileverified";
    public static String FB_ID = "fbid";
    public static String FIRST_NAME = "firstname";
    public static String LAST_NAME = "lastname";
    public static String EMAIL = "email";
    public static String PRO_PIC = "pro-pic";
    public static String ACTIVE_TEAM_ID = "activeTeamId";
    public static String MY_REFERRAL_CODE = "referralCode";
    public static String INVITED_BY = "invited_by";

    @NotNull
    public static final String DEFAULT_COUNTRY_CODE = "+91";

    private PreferenceHelper(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static void setFirstTimeLaunch() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
        editor.apply();
    }

    public Boolean containsKey(String key) {
        return pref.contains(key);
    }

    public void clearPrefs() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public static void initialize(Context appContext) {
        if (appContext == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (prefHelper == null) {
            synchronized (PreferenceHelper.class) {
                if (prefHelper == null) {
                    prefHelper = new PreferenceHelper(appContext);
                }
            }
        }
    }

    public static PreferenceHelper getInstance() {
        if (prefHelper == null) {
            throw new IllegalStateException(
                    "SharedPrefsManager is not initialized, call initialize(applicationContext) " +
                            "static method first");
        }
        return prefHelper;
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void removeKey(String key) {
        pref.edit().remove(key).apply();
    }

    public String getString(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public Boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public void setBoolean(String key, Boolean isBoolean) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, isBoolean);
        editor.apply();

    }


    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void loggedOut() {
        clearPrefs();
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }


}
