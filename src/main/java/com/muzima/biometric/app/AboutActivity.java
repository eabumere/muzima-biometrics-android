package com.muzima.biometric.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.muzima.biometric.R;
import com.muzima.biometric.util.ManifestData;
import com.muzima.biometric.widget.CustomItemAdapter;
import com.muzima.biometric.widget.HeaderItem;
import com.muzima.biometric.widget.ListItem;
import com.neurotec.biometrics.NBiometricEngine;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NDeviceType;
import com.neurotec.lang.NModule;
import com.neurotec.plugins.NPlugin;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class AboutActivity extends BaseListActivity {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = AboutActivity.class.getSimpleName();

	// ===========================================================
	// Private fields
	// ===========================================================
	private Bitmap mLogo;
	private String mAbout;

	// ===========================================================
	// Protected methods
	// ===========================================================


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLogo = BitmapFactory.decodeResource(getResources(), R.drawable.iris);
		mAbout = String.format(getString(R.string.msg_company_info), getString(R.string.msg_company_name), ManifestData.getApplicationVersion(this));
		NDeviceManager devMan = new NDeviceManager();
		NBiometricEngine nEng = new NBiometricEngine();
		devMan.setBiometricEngine(nEng);
	}

	@Override
	protected void onStart() {
		super.onStart();

		List<ListItem> items = new ArrayList<ListItem>();

		items.add(new ListItem(ManifestData.getApplicationName(this), mAbout, mLogo, true));
		items.add(new HeaderItem(getString(R.string.msg_loaded_modules)));

		if (NModule.getLoadedModules().length > 0) {
			for (NModule module : NModule.getLoadedModules()) {
				items.add(new ListItem(module.getName(), module.getTitle()));
			}
		} else {
			items.add(new ListItem(getString(R.string.msg_no_loaded_modules)));
		}

		items.add(new HeaderItem(getString(R.string.msg_plugins)));
		List<NPlugin> plugins = NDeviceManager.getPluginManager().getPlugins();

		if (plugins != null && plugins.size() > 0) {
			for (NPlugin plugin : plugins) {
				String status = "";
				if (plugin.getError() != null) {
					status = getString(R.string.msg_status_format, plugin.getError().getMessage());
				}
				items.add(new ListItem(plugin.getModule().getPluginName(), status));
			}
		} else {
			items.add(new ListItem(getString(R.string.msg_no_loaded_plugins)));
		}

		setListAdapter(new CustomItemAdapter(this, items));
	}

}
