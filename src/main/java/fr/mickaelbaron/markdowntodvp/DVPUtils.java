package fr.mickaelbaron.markdowntodvp;

import java.util.LinkedHashMap;
import java.util.Map;

public class DVPUtils {

    protected static final String IMAGE_DIRECTORY_DEFAULT = "images";

    public static String getRomanNumeralsFromNumber(int Int) {
	LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
	roman_numerals.put("M", 1000);
	roman_numerals.put("CM", 900);
	roman_numerals.put("D", 500);
	roman_numerals.put("CD", 400);
	roman_numerals.put("C", 100);
	roman_numerals.put("XC", 90);
	roman_numerals.put("L", 50);
	roman_numerals.put("XL", 40);
	roman_numerals.put("X", 10);
	roman_numerals.put("IX", 9);
	roman_numerals.put("V", 5);
	roman_numerals.put("IV", 4);
	roman_numerals.put("I", 1);
	String res = "";
	for (Map.Entry<String, Integer> entry : roman_numerals.entrySet()) {
	    int matches = Int / entry.getValue();
	    res += repeat(entry.getKey(), matches);
	    Int = Int % entry.getValue();
	}
	return res;
    }

    private static String repeat(String s, int n) {
	if (s == null) {
	    return null;
	}
	final StringBuilder sb = new StringBuilder();
	for (int i = 0; i < n; i++) {
	    sb.append(s);
	}
	return sb.toString();
    }

    public static String getLetterFromNumber(int i) {
	// return null for bad input
	if (i < 0) {
	    return null;
	}

	// convert to base 26
	String s = Integer.toString(i, 26);

	char[] characters = s.toCharArray();

	String result = "";
	for (char c : characters) {
	    // convert the base 26 character back to a base 10 integer
	    int x = Integer.parseInt(Character.valueOf(c).toString(), 26);
	    // append the ASCII value to the result
	    result += String.valueOf((char) (x + 'a'));
	}

	return result;
    }

    public static String replaceIntoImageDirectory(String src) {
	int lastIndexOf = src.lastIndexOf('/');
	if (lastIndexOf != -1) {
	    return IMAGE_DIRECTORY_DEFAULT
		    + src.substring(lastIndexOf, src.length());
	} else {
	    return IMAGE_DIRECTORY_DEFAULT + "/" + src;
	}
    }
}
