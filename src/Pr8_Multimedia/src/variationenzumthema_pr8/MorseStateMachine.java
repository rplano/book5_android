package variationenzumthema_pr8;

import java.util.HashMap;
import java.util.Map;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MorseStateMachine
 *
 * A simple class that converts Morse code to strings and vice versa.
 *
 * Genauer gilt Folgendes: <br/>
 * - Ein Dah ist dreimal so lang wie ein Dit.<br/>
 * - Die Pause zwischen zwei gesendeten Symbolen ist ein Dit lang.<br/>
 * - Zwischen Buchstaben in einem Wort wird eine Pause von der Länge eines Dah
 * (oder drei Dits) eingeschoben.<br/>
 * - Die Länge der Pause zwischen Wörtern entspricht sieben Dits.<br/>
 *
 * @see https://de.wikipedia.org/wiki/Morsezeichen#Zeitschema_und_Darstellung
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MorseStateMachine {

	private final String[] MORSE_ALPHABET = { "01", "1000", "1010", "100", "0", "0010", "110", "0000", "00", "0111",
			"101", "0100", "11", "10", "111", "0110", "1101", "010", "000", "1", "001", "0001", "011", "1001", "1011",
			"1100" };

	private Map<String, Character> morseLookupTable;

	public MorseStateMachine() {
		morseLookupTable = new HashMap<String, Character>();
		for (int i = 0; i < MORSE_ALPHABET.length; i++) {
			char c = (char) (i + 'A');
			morseLookupTable.put(MORSE_ALPHABET[i], c);
		}
	}

	public void init() {
		lCounter0 = -1;
		lCounter1 = -1;
		morseMsg = "";
		morseChar = "";
	}

	public void addNewString(String s) {
		convertMorseCodeToString(s);
		// System.out.println(morseMsg);
	}

	public String getMorseMessage() {
		if (morseMsg != null) {
			String tmp = morseMsg;
			morseMsg = "";
			return tmp;
		}
		return "";
	}

	private int lCounter0 = -1; // signal length counter
	private int lCounter1 = -1; // signal length counter

	private void convertMorseCodeToString(String morse) {
		// morseMsg = "";
		for (int i = 0; i < morse.length(); i++) {
			char c = morse.charAt(i);
			if (c == '=') {
				// we have a '1' signal
				if (lCounter1 > -1) {
					// last signal also was a '1'
					lCounter1++;

				} else {
					// last signal was a '0'
					evaluateSignal('0', lCounter0);
					lCounter0 = -1;
					lCounter1 = 1;
				}

			} else {
				// we have a '0' signal
				if (lCounter0 > -1) {
					// last signal also was a '0'
					lCounter0++;

				} else {
					// last signal was a '1'
					evaluateSignal('1', lCounter1);
					lCounter1 = -1;
					lCounter0 = 1;
				}
			}
		}
		evaluateSignal('0', lCounter0);
		// return morseMsg;
	}

	private String morseMsg = "";
	private String morseChar = "";

	private void evaluateSignal(char signal, int count) {
		if (signal == '0' && count > 4) {
			// new word
			if (morseLookupTable.containsKey(morseChar)) {
				// System.out.print(morseLookupTable.get(morseChar));
				// System.out.print(" ");
				if (morseLookupTable.containsKey(morseChar)) {
					morseMsg += morseLookupTable.get(morseChar) + " ";
				}
			}
			morseChar = "";
		} else if (signal == '0' && count > 1) {
			// new morse character
			// System.out.print(morseLookupTable.get(morseChar));
			if (morseLookupTable.containsKey(morseChar)) {
				morseMsg += morseLookupTable.get(morseChar);
			}
			morseChar = "";
		} else {
			if (signal == '0') {
				// do nothing
			} else if (signal == '1' && count > 1) {
				morseChar += "1";
			} else if (signal == '1' && count == 1) {
				morseChar += "0";
			}
		}
		// System.out.println(signal+count);
	}

	public String convertStringToMorseCode(String msg) {
		msg = msg.toUpperCase();
		String morse = "_______";
		for (int i = 0; i < msg.length(); i++) {
			char c = msg.charAt(i);
			int idx = c - 'A';
			if (c == ' ') {
				morse += "____";
			} else if (idx >= 0 && idx < MORSE_ALPHABET.length) {
				morse += convertToDitsAndDahs(MORSE_ALPHABET[idx]) + "__";
			} else {
				System.out.println("error");
			}
		}
		morse += "____";
		return morse;
	}

	private String convertToDitsAndDahs(String letter) {
		String ditsAndDahs = "";
		for (int i = 0; i < letter.length(); i++) {
			char c = letter.charAt(i);
			if (c == '0') {
				// dit
				ditsAndDahs += "=_";
			} else if (c == '1') {
				// dah
				ditsAndDahs += "===_";
			} else {
				System.out.println("error");
			}
		}
		return ditsAndDahs;
	}

	public static void main(String[] args) {
		// init
		MorseStateMachine msm = new MorseStateMachine();

		// encode
		String msg = "MORSE CODE";
		String morse = msm.convertStringToMorseCode(msg.toUpperCase());
		System.out.println(morse);

		// decode
		msm.addNewString("_______===_===___===_===_===___=_===_=___=_=_=___=_______===_=_===_=___==");
		System.out.println(msm.getMorseMessage());
		msm.addNewString("=_===_===___===_=_=___=_______");
		System.out.println(msm.getMorseMessage());
	}
}
