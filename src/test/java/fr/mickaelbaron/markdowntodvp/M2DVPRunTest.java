package fr.mickaelbaron.markdowntodvp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Mickael BARON
 */
public class M2DVPRunTest {

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
    public void mainWithoutParameterTest() throws IOException {
	M2DVPRun.main(null);
	Assert.assertEquals("No input file specified.\n", errContent.toString());
    }
    
    @Test
    public void mainWithOneParameterTest() throws IOException {
	java.net.URL url = M2DVPRunTest.class.getResource("/withdocument/markitdown-sample.text");
	String[] stringArrays = { url.getFile() };
	M2DVPRun.main(stringArrays);
	Assert.assertEquals("", errContent.toString());
    }
    
    @Test
    public void mainWithUnknownFilenameTest() throws IOException {
	String[] stringArrays = { "bad.text" };
	M2DVPRun.main(stringArrays);
	Assert.assertEquals("file filename not found.\n", errContent.toString());
    }
}
