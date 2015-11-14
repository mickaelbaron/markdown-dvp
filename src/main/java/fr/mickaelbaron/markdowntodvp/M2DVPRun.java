package fr.mickaelbaron.markdowntodvp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Decorator;
import com.github.rjeschke.txtmark.DefaultDecorator;
import com.github.rjeschke.txtmark.Processor;

/**
 * Simple class for processing markdown files on the command line.
 * 
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * <code>java -jar markdowntodvp.jar filename --headerfooterfile=header_footer_file --format=dvp --useextension=true
 * </code>
 * </pre>
 * 
 * <p>
 * The <code>header_footer_file</code> is an optional UTF-8 encoded file
 * containing a header and a footer to output around the generated HTML code.
 * </p>
 * 
 * <p>
 * The following line separates header from footer.
 * </p>
 * 
 * <pre>
 * &lt;!-- ### -->
 * </pre>
 * 
 * @author Mickael BARON <baron.mickael@gmail.com>
 */
public class M2DVPRun {

    private static final String HEADER_FOOTER_KEY = "headerfooterfile";
    
    private static final String FORMAT_KEY = "format";
    
    private static final String USE_EXTENSION_KEY = "useextension";
    
    private static final String[] KEY_OPTIONS = { HEADER_FOOTER_KEY,
	    FORMAT_KEY, USE_EXTENSION_KEY };

    private static final String DVP_FORMAT = "dvp";
    
    public static void main(String[] args) throws IOException {
	// This is just a _hack_ ...
	BufferedReader reader = null;
	if (args == null || args.length == 0) {
	    System.err.print("No input file specified.");
	    return;
	}

	Map<String, String> optionValues = new HashMap<String, String>();
	if (args.length > 1) {
	    for (int i = 1; i < args.length; i++) {
		for (String currentKeyOptions : KEY_OPTIONS) {
		    if (args[i].startsWith("--" + currentKeyOptions + "=")
			    && (args[i].length() > (currentKeyOptions.length()
				    + 3))) {
			String substring = args[i]
						.substring(currentKeyOptions.length() + 3);
			if (substring != null && !substring.isEmpty()) {
			    optionValues.put(currentKeyOptions, substring);			    
			}
		    }
		}
	    }
	}

	if (optionValues.containsKey(HEADER_FOOTER_KEY)) {
	    reader = new BufferedReader(new InputStreamReader(
		    new FileInputStream(optionValues.get(HEADER_FOOTER_KEY)), "UTF-8"));
	    String line = reader.readLine();
	    while (line != null && !line.startsWith("<!-- ###")) {
		System.out.println(line);
		line = reader.readLine();
	    }
	}
	
	Decorator current = null;
	if (optionValues.containsKey(FORMAT_KEY)) {
	    String formatValue = optionValues.get(FORMAT_KEY);
	    
	    if (DVP_FORMAT.equalsIgnoreCase(formatValue)) {
		current = new DVPDecorator();
	    } else {
		current = new DefaultDecorator();
	    }	    
	} else {
	    current = new DefaultDecorator();
	}
	
	Configuration build;
	
	if (optionValues.containsKey(USE_EXTENSION_KEY)) {
	    String useExtensionValue = optionValues.get(USE_EXTENSION_KEY);
	    
	    if ("true".equalsIgnoreCase(useExtensionValue)) {
		build = Configuration.builder().forceExtentedProfile().setCodeBlockEmitter(new CodeBlockEmitter()).setDecorator(current).build();
	    } else {
		build = Configuration.builder().setDecorator(current).build();
	    }
	} else {
	    build = Configuration.builder().setDecorator(current).build();
	}
	
	try {
	    System.out.println(Processor.process(new File(args[0]), build));	    
	} catch(IOException e) {
	    System.err.print("file " + args[0] + " not found.");
	    return;
	}

	if ((optionValues.containsKey("headerfooterfile")) && reader != null) {
	    String line = reader.readLine();
	    while (line != null) {
		System.out.println(line);
		line = reader.readLine();
	    }
	    reader.close();
	}
    }
}
