package variationenzumthema_pr2;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * SettingsActivity
 * 
 * This activity show how to use preferences.
 * 
 * @see https://stackoverflow.com/questions/531427/how-do-i-display-the-current-value-of-an-android-preference-in-the-preference-su/11609799
 * @see http://www.VariationenZumThema.de/
 * @author EddieB & Ralph P. Lano
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		// nasty trick to change background color:
		View root = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
		root.setBackgroundColor(0x200000ff);

		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
		initSummary(getPreferenceScreen());

		// how to read preferences:
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Map<String, ?> allPreferences = sharedPreferences.getAll();
		for (String key : allPreferences.keySet()) {
			Log.i("SettingsActivity", "key:" + key + ", value=" + allPreferences.get(key));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		updatePrefSummary(findPreference(key));
	}

	private void initSummary(Preference p) {
		if (p instanceof PreferenceGroup) {
			PreferenceGroup pGrp = (PreferenceGroup) p;
			for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
				initSummary(pGrp.getPreference(i));
			}
		} else {
			updatePrefSummary(p);
		}
	}

	private void updatePrefSummary(Preference p) {
		if (p instanceof ListPreference) {
			ListPreference listPref = (ListPreference) p;
			p.setSummary(listPref.getEntry());
		} else if (p instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			if (p.getTitle().toString().toLowerCase().contains("password")) {
				p.setSummary("******");
			} else {
				p.setSummary(editTextPref.getText());
			}
		} else if (p instanceof MultiSelectListPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary(editTextPref.getText());
		} else if (p instanceof NumberPickerPreference) {
			NumberPickerPreference listPref = (NumberPickerPreference) p;
			p.setSummary("" + listPref.getValue());
		}
	}
}