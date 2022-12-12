package variationenzumthema_pr7;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * CellTowerActivity
 *
 * This activity lists available cell towers nearby.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CellTowerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.setText("");

		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

		List<CellInfo> cells = telephonyManager.getAllCellInfo();
		for (CellInfo cellInfo : cells) {
			String type = "none";
			CellSignalStrength strength = null;
			boolean registered = false;
			int latitude = 0;
			int longitude = 0;
			int cellid = 0;
			int celllac = 0;

			if (cellInfo instanceof CellInfoCdma) {
				type = "Cdma";
				CellInfoCdma ci = (CellInfoCdma) cellInfo;
				strength = ci.getCellSignalStrength();
				registered = ci.isRegistered();

				CellIdentityCdma identity = ci.getCellIdentity();
				cellid = identity.getNetworkId();
				latitude = identity.getLatitude();
				longitude = identity.getLongitude();

			} else if (cellInfo instanceof CellInfoGsm) {
				type = "Gsm";
				CellInfoGsm ci = (CellInfoGsm) cellInfo;
				strength = ci.getCellSignalStrength();
				registered = ci.isRegistered();

				CellIdentityGsm identity = ci.getCellIdentity();
				cellid = identity.getCid();
				celllac = identity.getLac();

			} else if (cellInfo instanceof CellInfoLte) {
				type = "Lte";
				CellInfoLte ci = (CellInfoLte) cellInfo;
				strength = ci.getCellSignalStrength();
				registered = ci.isRegistered();

				CellIdentityLte identity = ci.getCellIdentity();
				cellid = identity.getCi();
				// celllac = identity.getLac();

			} else if (cellInfo instanceof CellInfoWcdma) {
				type = "Wcdma";
				CellInfoWcdma ci = (CellInfoWcdma) cellInfo;
				strength = ci.getCellSignalStrength();
				registered = ci.isRegistered();

				CellIdentityWcdma identity = ci.getCellIdentity();
				cellid = identity.getCid();
				celllac = identity.getLac();

			} else {
				Log.e("CellTowerActivity", "Unknown CellInfo subclass.");
			}

			if (strength != null) {
				int db = strength.getDbm();
				String msg = "" + cellid + ": ";
				// msg += "celllac=" + celllac + ", ";
				// msg += "lon=" + longitude + ", ";
				// msg += "lat=" + latitude + ", ";

				msg += "type=" + type + ", ";
				msg += "reg=" + registered + ", ";
				msg += "db=" + db + "\n";
				tv.append(msg);
				Log.i("CellTowerActivity", "" + cellInfo.toString());
			}
		}

	}
}
