package com.example.anhqu.foody;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.anhqu.foody.model.User;
import com.google.android.gms.location.places.Place;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    private static final String PREF_NAME = "session";

    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String KEY_ID = "id";

    private static final String KEY_NAME = "username";

    private static final String KEY_PW = "pw";

    private static final String KEY_FNAME = "fullname";

    private static final String KEY_ML = "mobile";

    private static final String KEY_PLACE = "place";

    private static final String KEY_ADD = "adress";

    private static final String KEY_LATLNG = "latlng";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void create(int id, String name, String pw, String fname, String phone) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PW, pw);
        editor.putString(KEY_FNAME, fname);
        editor.putString(KEY_ML, phone);
        editor.apply();
    }

    public void putPlace(Place place) {
        editor.putString(KEY_PLACE, (String) place.getName());
        editor.putString(KEY_ADD, (String) place.getAddress());
        editor.putString(KEY_LATLNG, String.valueOf(place.getLatLng()));
        editor.apply();
    }

    public User getUser() {
        int id = pref.getInt(KEY_ID, 0);
        String username = pref.getString(KEY_NAME, null);
        String pw = pref.getString(KEY_PW, null);
        String fname = pref.getString(KEY_FNAME, null);
        String mobile = pref.getString(KEY_ML, null);
        return new User(id, username, pw, fname, mobile);
    }

    public String getAddress() {
        return pref.getString(KEY_ADD, null);
    }

    public int getUserId(){
        return pref.getInt(KEY_ID, 0);
    }

    public void signOut() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}