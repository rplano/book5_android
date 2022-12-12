package variationenzumthema_st4;

import java.nio.ByteBuffer;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Util
 *
 * This is a simple utility class turning byte arrays into HEX strings and vice versa.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Util {

	public static byte[] longToBytes(long l) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / 8);
		buffer.putLong(l);
		return buffer.array();
	}

	public static String byteArrayToHexString(byte[] buffer) {
	    StringBuilder sb = new StringBuilder();
	    for (byte b : buffer) {
	        sb.append(String.format("%02X", b));
	    }
	    return sb.toString();
	}

	public static byte[] hexStringToByteArray(String hexString) {
		int len = hexString.length();
		byte[] buffer = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			buffer[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return buffer;
	}
}
