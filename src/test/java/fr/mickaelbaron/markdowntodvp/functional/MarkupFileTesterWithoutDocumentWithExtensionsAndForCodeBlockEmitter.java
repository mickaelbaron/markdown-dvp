package fr.mickaelbaron.markdowntodvp.functional;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;

import fr.mickaelbaron.markdowntodvp.CodeBlockEmitter;
import fr.mickaelbaron.markdowntodvp.DVPDecorator;

/**
 * @author Mickael BARON
 */
@RunWith(value = Parameterized.class)
public class MarkupFileTesterWithoutDocumentWithExtensionsAndForCodeBlockEmitter
	extends AbstractMarkupFileTester {

    protected static String[] testFilenames = new String[] {
	    "fencedcodebloc.txt", "fencedcodeblocwithparagraph.txt",
	    "fencedcodeblocwithmixflavours.txt" };

    @Parameters
    public static Collection<Object[]> testResultPairs() throws IOException {
	return AbstractMarkupFileTester.testResultStringPairs(
		"/withoutdocument/withextensions", testFilenames);
    }

    public MarkupFileTesterWithoutDocumentWithExtensionsAndForCodeBlockEmitter(
	    final TestResultPair pair) {
	super(pair);

	build = Configuration.builder().forceExtentedProfile()
		.setCodeBlockEmitter(new CodeBlockEmitter())
		.setDecorator(new DVPDecorator()).build();
    }

    @Test
    public void runTest() {
	String trim = Processor.process(pair.getTest(), build).trim();
	assertEquals(pair.toString(), pair.getResult().trim(), trim);
    }
}
