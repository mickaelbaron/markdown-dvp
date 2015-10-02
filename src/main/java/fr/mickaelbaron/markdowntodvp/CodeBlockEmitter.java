package fr.mickaelbaron.markdowntodvp;

import java.util.List;

import com.github.rjeschke.txtmark.BlockEmitter;

public class CodeBlockEmitter implements BlockEmitter {

    @Override
    public void emitBlock(StringBuilder out, List<String> lines, String meta) {
	out.append("<code");
	if (meta.isEmpty()) {
	    meta = "other";
	} 
	out.append(" langage=\"" + meta + "\"" + " showLines=\"1\"><![CDATA[");
	for (final String s : lines) {
	    for (int i = 0; i < s.length(); i++) {
		final char c = s.charAt(i);
		switch (c) {
		case '&':
		    out.append("&amp;");
		    break;
		case '<':
		    out.append("&lt;");
		    break;
		case '>':
		    out.append("&gt;");
		    break;
		default:
		    out.append(c);
		    break;
		}
	    }
	    out.append('\n');
	}
	out.append("]]></code>\n");
    }

}

