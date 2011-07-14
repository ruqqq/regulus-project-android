package sg.rp.geeks.leoapp;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.preference.*;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.InputMethod;
import android.widget.EditText;
import android.widget.ListView;

public class PreferencesActivity extends PreferenceActivity {
    private ListView preferenceView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_preference);

        preferenceView = (ListView) findViewById(android.R.id.list);

        PreferenceScreen screen = createPreferenceHierarchy();
        screen.bind(preferenceView);
        preferenceView.setAdapter(screen.getRootAdapter());

        setPreferenceScreen(screen);
    }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // Login Settings
        PreferenceCategory catAuthentication = new PreferenceCategory(this);
        catAuthentication.setTitle("Authentication Settings");
        root.addPreference(catAuthentication);

        final EditTextPreference prefUsername = new EditTextPreference(this);
        prefUsername.setKey("pref_key_rp_username");
        prefUsername.setPersistent(true);
        prefUsername.setTitle("RP Username");
        String username = prefs.getString("pref_key_rp_username", "");
        if (username.equals("")) prefUsername.setSummary("Not set");
        else {
            prefUsername.setDefaultValue(username);
            prefUsername.setSummary(username);
        }
        prefUsername.setDialogMessage("Your RP Username:");
        prefUsername.getEditText().setRawInputType(InputType.TYPE_CLASS_NUMBER);
        prefUsername.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (!o.equals("")) prefUsername.setSummary((String) o);
                else prefUsername.setSummary("Not set");
                return true;
            }
        });
        catAuthentication.addPreference(prefUsername);

        final EditTextPreference prefPassword = new EditTextPreference(this);
        prefPassword.setKey("pref_key_rp_password");
        prefPassword.setPersistent(true);
        prefPassword.setTitle("RP Password");
        String password = prefs.getString("pref_key_rp_password", "");
        if (password.equals("")) prefPassword.setSummary("Not set");
        else {
            prefPassword.setDefaultValue(password);
            prefPassword.setSummary("(masked)");
        }
        prefPassword.setDialogMessage("Your RP Password:");
        prefPassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (!o.equals("")) prefPassword.setSummary("(masked)");
                else prefPassword.setSummary("Not set");
                return true;
            }
        });
        catAuthentication.addPreference(prefPassword);

        // About
        PreferenceCategory catAbout = new PreferenceCategory(this);
        catAbout.setTitle("About");
        root.addPreference(catAbout);

        Preference prefApplicationVersion = new Preference(this);
        prefApplicationVersion.setTitle("LeoApp v0.1");
        prefApplicationVersion.setSummary("©2011 Geeks@RP");
        prefApplicationVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/geeksatrp"));
                startActivity(i);
                return false;
            }
        });
        catAbout.addPreference(prefApplicationVersion);

        return root;
    }
}
