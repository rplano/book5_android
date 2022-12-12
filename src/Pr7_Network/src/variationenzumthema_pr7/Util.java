package variationenzumthema_pr7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Util
 *
 * A simple utility class with several useful methods for downloading web pages
 * and finding IPs.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Util {

	public static String getWebpage(String address) {
		try {
			URL url = new URL(address);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer content = new StringBuffer();
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				content.append(line);
			}
			br.close();
			con.disconnect();

			return content.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InetAddress getMyLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();

					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
						return inetAddress;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Takes the local address, e.g. 192.168.12.13, and turns it into a
	 * broadcast address, i.e. 192.168.12.255. global broadcast: would be
	 * "255.255.255.255"
	 */
	public static InetAddress getLocalBroadcastAddress() {
		try {
			InetAddress myIP = getMyLocalIpAddress();
			byte[] localAddress = myIP.getAddress();
			localAddress[3] = (byte) 255;
			return InetAddress.getByAddress(localAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String byteArrayToHexString(byte[] buffer) {
		StringBuilder sb = new StringBuilder();
		for (byte b : buffer) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static int byteArrayToLittleEndianInt(byte[] b) {
		final ByteBuffer bb = ByteBuffer.wrap(b);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getInt();
	}

	public static int byteArrayToBigEndianInt(byte[] b) {
		final ByteBuffer bb = ByteBuffer.wrap(b);
		bb.order(ByteOrder.BIG_ENDIAN);
		return bb.getInt();
	}
}
