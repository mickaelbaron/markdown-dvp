package fr.mickaelbaron.markdowntodvp.functional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.rjeschke.txtmark.Configuration;

import fr.mickaelbaron.markdowntodvp.DVPDecorator;
import fr.mickaelbaron.markdowntodvp.M2DVPRun;

/**
 * @author Mickael BARON
 */
@RunWith(value = Parameterized.class)
public class MarkupFileTesterWithDocument extends AbstractMarkupFileTester {

//    protected static String[] testFilenames = new String[] { 
//	    "daringfireball-markdowndocumentation-sample",
//	    "markitdown-sample"};

    protected static String[] testFilenames = new String[] { 
	    "index"};
    
    @Parameters
    public static Collection<Object[]> testResultPairs() throws IOException, URISyntaxException {
	return AbstractMarkupFileTester.testResultFilePairs("/withdocument", testFilenames);
    }

    public MarkupFileTesterWithDocument(final TestResultPair pair) {
	super(pair);

	build = Configuration.builder().setDecorator(new DVPDecorator()).build();
    }

    @Test
    public void runTest() throws URISyntaxException, IOException {	
	java.net.URL url = AbstractMarkupFileTester.class.getResource("/withdocument/headerfootersample.txt");
	java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
	String[] args = { pair.getTest(), "--headerfooterfile=" + resPath.toString(), "--format=DVP" };
	M2DVPRun.main(args);	
    }
}
