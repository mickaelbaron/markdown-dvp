package fr.mickaelbaron.markdowntodvp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.rjeschke.txtmark.DefaultDecorator;
import com.github.rjeschke.txtmark.Utils;

/**
 * @author Mickael BARON
 */
public class DVPDecorator extends DefaultDecorator {

    private char[] pattern = { 'I', 'a', '1', '1', '1', '1' };

    private boolean[] sectionState = { false, false, false, false, false,
	    false };

    private int[] sectionIndex = { 0, 0, 0, 0, 0, 0 };

    private int currentEmbeddedList = 0;

    private String getRomanNumeralsFromNumber(int Int) {
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

    private String getLetterFromNumber(int i) {
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

    public static String repeat(String s, int n) {
	if (s == null) {
	    return null;
	}
	final StringBuilder sb = new StringBuilder();
	for (int i = 0; i < n; i++) {
	    sb.append(s);
	}
	return sb.toString();
    }

    @Override
    public void openParagraph(StringBuilder out) {
	out.append("<paragraph>");
    }

    @Override
    public void closeParagraph(StringBuilder out) {
	out.append("</paragraph>\n");
    }

    @Override
    public void openStrong(StringBuilder out) {
	out.append("<b>");
    }

    @Override
    public void closeStrong(StringBuilder out) {
	out.append("</b>");
    }

    @Override
    public void openEmphasis(StringBuilder out) {
	out.append("<i>");
    }

    @Override
    public void closeEmphasis(StringBuilder out) {
	out.append("</i>");
    }

    @Override
    public void openStrike(StringBuilder out) {
	out.append("<s>");
    }

    @Override
    public void closeStrike(StringBuilder out) {
	out.append("</s>");
    }

    @Override
    public void openSuper(StringBuilder out) {
	out.append("<sup>");
    }

    @Override
    public void closeSuper(StringBuilder out) {
	out.append("</sup>");
    }

    @Override
    public void openHeadline(StringBuilder out, int level) {
	if (sectionState[level - 1]) {
	    for (int i = (level - 1); i < sectionState.length; i++) {
		if (sectionState[i]) {
		    out.append("</section>\n");
		    sectionState[i] = false;
		}
	    }
	    for (int i = level; i < sectionIndex.length; i++) {
		sectionIndex[i] = 0;
	    }
	}

	sectionIndex[level - 1]++;
	out.append("<section id=\"" + displayHeadline(level) + "\">\n");
	out.append("<title>");
	sectionState[level - 1] = true;
    }

    private String displayLevel(int level, int sectionNumber) {
	char currentChar = this.pattern[level];

	switch (currentChar) {
	case 'I':
	    return this.getRomanNumeralsFromNumber(sectionNumber);
	case '1':
	    return Integer.toString(sectionNumber);
	case 'a':
	    return this.getLetterFromNumber(sectionNumber - 1);
	default:
	    return Integer.toString(sectionNumber);
	}
    }

    private String displayHeadline(int level) {
	StringBuffer value = new StringBuffer();
	for (int i = 0; i <= (level - 1); i++) {
	    value.append(displayLevel(i, sectionIndex[i]));
	    if (i != (level - 1))
		value.append(".");
	}
	return value.toString();
    }

    @Override
    public void closeHeadline(StringBuilder out, int level) {
	out.append("</title>\n");
    }

    static String readFile(String path, Charset encoding) throws IOException {
	byte[] encoded = Files.readAllBytes(Paths.get(path));
	return new String(encoded, encoding);
    }

    @Override
    public void openDocument(StringBuilder out, boolean enableDocument) {
	if (enableDocument) {
	    out.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
	    out.append("<document>");

	    // Add head part.
	    // Add authors part.
	    // Add synopsis part.
	}
    }

    @Override
    public void closeDocument(StringBuilder out, boolean enableDocument) {
	for (boolean current : sectionState) {
	    if (current) {
		out.append("</section>\n");
	    }
	}

	if (enableDocument) {
	    out.append("</document>");
	}
    }

    @Override
    public void openCodeSpan(StringBuilder out) {
	out.append("<inline>");
    }

    @Override
    public void closeCodeSpan(StringBuilder out) {
	out.append("</inline>");
    }

    @Override
    public void openBlockquote(StringBuilder out) {
	out.append("<citation>");
    }

    @Override
    public void closeBlockquote(StringBuilder out) {
	out.append("</citation>\n");
    }

    @Override
    public void openCodeBlock(StringBuilder out) {
	out.append("<code showLines=\"1\"><![CDATA[");
    }

    @Override
    public void closeCodeBlock(StringBuilder out) {
	out.append("]]></code>\n");
    }

    @Override
    public void openListItem(StringBuilder out) {
	out.append("<element useText=\"0\"");
    }

    @Override
    public void closeListItem(StringBuilder out) {
	out.append("</element>\n");
    }

    @Override
    public void openUnorderedList(StringBuilder out) {
	if (currentEmbeddedList != 0) {
	    out.append("\n<liste");
	} else {
	    out.append("<liste");
	}

	out.append(">\n");

	currentEmbeddedList++;
    }

    @Override
    public void closeUnorderedList(StringBuilder out) {
	out.append("</liste>\n");
	currentEmbeddedList--;
    }

    @Override
    public void openLink(StringBuilder out) {
	out.append("<link");
    }

    @Override
    public void closeLink(StringBuilder out) {
	out.append("</link>");
    }

    @Override
    public void openImage(StringBuilder out, String src, String alt,
	    String title) {
	out.append("<image");
	out.append(" src=\"");
	Utils.appendValue(out, src, 0, src.length());
	out.append("\" alt=\"");
	Utils.appendValue(out, alt, 0, alt.length());
	out.append('"');
	if (title != null) {
	    out.append(" titre=\"");
	    Utils.appendValue(out, title, 0, title.length());
	    out.append('"');
	}
    }
}
