package fr.mickaelbaron.markdowntodvp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.rjeschke.txtmark.DefaultDecorator;
import com.github.rjeschke.txtmark.Utils;

/**
 * @author Mickael BARON
 */
public class DVPDecorator extends DefaultDecorator {

    protected final String codeLanguageDefault = "other";
    
    protected char[] pattern = { 'I', 'a', '1', '1', '1', '1' };

    protected boolean[] sectionState = { false, false, false, false, false,
	    false };

    protected int[] sectionIndex = { 0, 0, 0, 0, 0, 0 };

    protected int currentEmbeddedList = 0;

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

    protected String displayLevel(int level, int sectionNumber) {
	char currentChar = this.pattern[level];

	switch (currentChar) {
	case 'I':
	    return DVPUtils.getRomanNumeralsFromNumber(sectionNumber);
	case '1':
	    return Integer.toString(sectionNumber);
	case 'a':
	    return DVPUtils.getLetterFromNumber(sectionNumber - 1);
	default:
	    return Integer.toString(sectionNumber);
	}
    }

    protected String displayHeadline(int level) {
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
    public void closeDocument(StringBuilder out) {
	for (boolean current : sectionState) {
	    if (current) {
		out.append("</section>\n");
	    }
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
	out.append("<code langage=\"" + codeLanguageDefault + "\" showLines=\"1\"><![CDATA[");
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
