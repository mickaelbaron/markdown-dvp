package fr.mickaelbaron.markdowntodvp.functional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.mickaelbaron.markdowntodvp.M2DVPRun;

/**
 * @author Mickael BARON
 */
public class MarkupFileTesterWithDocument {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    private PrintStream originalOut;
    
    private PrintStream originalErr;
    
    @Before
    public void setUpStreams() {
	originalOut = System.out;
	originalErr = System.err;
	
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @Test
    public void runTest() throws URISyntaxException, IOException {
	java.net.URL file = AbstractMarkupFileTester.class.getResource("/withdocument/daringfireball-markdowndocumentation-sample.text");
	java.net.URL header = AbstractMarkupFileTester.class.getResource("/withdocument/headerfootersample.txt");
	java.nio.file.Path resPath = java.nio.file.Paths.get(header.toURI());
	java.nio.file.Path filePath = java.nio.file.Paths.get(file.toURI());
	
	M2DVPRun.main(new String[]{ filePath.toString(), "--headerfooterfile=" + resPath.toString(), "--format=DVP", "--useextension=true" });
	Assert.assertEquals("", errContent.toString());
	Assert.assertTrue(outContent.toString().contains("<paragraph>Any number of underlining <inline>=</inline>'s or <inline>-</inline>'s will work.</paragraph>"));
	Assert.assertTrue(outContent.toString().contains("</document>"));
	
	file = AbstractMarkupFileTester.class.getResource("/withdocument/markitdown-sample.text");
	filePath = java.nio.file.Paths.get(file.toURI());
	M2DVPRun.main(new String[]{ filePath.toString(), "--headerfooterfile=" + resPath.toString(), "--format=DVP", "--useextension=true" });
	Assert.assertEquals("", errContent.toString());
	Assert.assertTrue(outContent.toString().contains("<element useText=\"0\">Sometimes you just want a URL like <link href=\"http://www.markitdown.net/\">http://www.markitdown.net/</link>.</element>"));
	Assert.assertTrue(outContent.toString().contains("</document>"));
    }
}
