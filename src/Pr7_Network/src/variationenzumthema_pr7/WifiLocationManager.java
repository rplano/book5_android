package variationenzumthema_pr7;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.net.wifi.ScanResult;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * WifiLocationManager
 * 
 * Uses signal strength of wifi signal to estimate location. Uses a voting
 * system if several access points are available. Only works well with more than
 * five access points available.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WifiLocationManager {
	// assume -100db is smallest
	private static final int DB_MAX = -100;

	// basically a standard deviation for still acceptable values, like error in measuerement
	// i.e. if AP1 has db of 64, the a value of 64 +/- 4 is still considered to be AP1 
	private int dbFuzzy = 4;

	// String is bssid, first integer is location-id, second is signal-strength
	private Map<String, Map<Integer, Integer>> accessPoints;

	// bssid, signals
	private int[][] allSignalStrengths;

	public WifiLocationManager() {
		accessPoints = new HashMap<String, Map<Integer, Integer>>();
	}

	public void addScanResults(List<ScanResult> wifiScanResultList, int loctn) {
		for (ScanResult result : wifiScanResultList) {
			String bssid = result.BSSID;
			int signalLevel = result.level;
			if (!accessPoints.containsKey(bssid)) {
				accessPoints.put(bssid, new HashMap<Integer, Integer>());
			}
			Map<Integer, Integer> signalLevels = accessPoints.get(bssid);
			if (signalLevels.containsKey(loctn)) {
				int oldLevel = signalLevels.get(loctn);
				int avg = (oldLevel + signalLevel) / 2;
				signalLevels.put(loctn, avg);
			} else {
				signalLevels.put(loctn, signalLevel);
			}
			// accessPoints.put(bssid, signalLevel);
		}
	}

	// try to estimate location of test signal
	public TreeMap<Integer, Integer> getLocationEstimates(List<ScanResult> wifiScanResultList) {
		Map<String, Integer> bssidStrength = new HashMap<String, Integer>();
		for (ScanResult result : wifiScanResultList) {
			String bssid = result.BSSID;
			int signalLevel = result.level;
			bssidStrength.put(bssid, signalLevel);
		}

		int nrAPs = accessPoints.size();
		allSignalStrengths = new int[nrAPs][findNrOfLocations()];
		int[] newSignal = new int[nrAPs];

		convertMapsToArrays(accessPoints, bssidStrength, allSignalStrengths, newSignal);

		int[] votes = new int[allSignalStrengths[0].length];
		estimateLocationOfSignal(newSignal, votes);

		TreeMap<Integer, Integer> voteMap = new TreeMap<Integer, Integer>(Collections.reverseOrder());
		for (int i = 0; i < votes.length; i++) {
			voteMap.put(votes[i], i);
		}

		return voteMap;
	}

	private void estimateLocationOfSignal(int[] newSignal, int[] votes) {
		for (int id = 0; id < newSignal.length; id++) {
			estimateLocation(id, newSignal[id], votes);
		}
	}

	private void estimateLocation(int id, int signalStrength, int[] votes) {
		for (int j = 0; j < allSignalStrengths[0].length; j++) {
			int sigStr = allSignalStrengths[id][j];
			if (sigStr < 0) { // db is negative
				int ds = (signalStrength - sigStr);
				ds = ds * ds;

				if (ds < (dbFuzzy * dbFuzzy)) {
					votes[j]++;
				}
			}
		}
	}

	private void convertMapsToArrays(Map<String, Map<Integer, Integer>> accessPoints,
			Map<String, Integer> bssidStrength, int[][] allSignalStrengths, int[] newSignal) {
		int count = 0;
		for (String bssid : bssidStrength.keySet()) {
			if (accessPoints.containsKey(bssid)) {
				int strength = bssidStrength.get(bssid);
				newSignal[count] = strength;
				Map<Integer, Integer> locStrength = accessPoints.get(bssid);
				for (int i = 0; i < findNrOfLocations(); i++) {
					if (locStrength.get(i) != null) {
						allSignalStrengths[count][i] = locStrength.get(i);
					}
				}
				count++;
			}
		}
	}

	public String listAccessPoints() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of wifi connections: " + accessPoints.size() + "\n");
		for (String bssid : accessPoints.keySet()) {
			sb.append(bssid.replace(":", "") + ":");
			Map<Integer, Integer> signalLevels = accessPoints.get(bssid);
			// int signalLevel =
			for (int i = 0; i < findNrOfLocations(); i++) {

				// }
				// for (int loctnId : signalLevels.keySet()) {
				if (signalLevels.containsKey(i)) {
					int signalLevel = signalLevels.get(i);
					sb.append(signalLevel + ",");
				} else {
					sb.append("___,");
				}
			}
			sb.append("dB\n");
		}
		return sb.toString();
	}

	private int findNrOfLocations() {
		int maxNrOfLocs = 0;
		for (String bssid : accessPoints.keySet()) {
			Map<Integer, Integer> signalLevels = accessPoints.get(bssid);
			int nrLocs = signalLevels.keySet().size();
			if (maxNrOfLocs < nrLocs) {
				maxNrOfLocs = nrLocs;
			}
		}
		return maxNrOfLocs;
	}
}
