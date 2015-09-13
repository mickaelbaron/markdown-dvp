package fr.mickaelbaron.markdowntodvp.functional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.rjeschke.txtmark.Configuration;

/**
 * @author Mickael BARON
 */
public abstract class AbstractMarkupFileTester {

    protected TestResultPair pair;

    protected static String[] testFilenames;

    protected Configuration build;

    public AbstractMarkupFileTester(final TestResultPair pair) {
	this.pair = pair;
    }

    public static Collection<Object[]> testResultFilePairs(String path, String[] fileNames)
	    throws IOException, URISyntaxException {
	final List<TestResultPair> fullResultPairList = new ArrayList<TestResultPair>();
	
	for (String string : fileNames) {
	    String name = path + File.separator + string + ".text";
	    java.net.URL url = AbstractMarkupFileTester.class.getResource(name);
	    java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
	    byte[] encoded = Files.readAllBytes(resPath);
	    String content = new String(encoded, "UTF-8");
	    
	    fullResultPairList.add(new TestResultPair(string, resPath.toString(), content));
	}

	final Collection<Object[]> testResultPairs = new ArrayList<Object[]>();
	for (final TestResultPair p : fullResultPairList) {
	    testResultPairs.add(new Object[] { p });
	}
	return testResultPairs;
    }
    
    public static Collection<Object[]> testResultStringPairs(String path, String[] fileNames)
	    throws IOException {
	final List<TestResultPair> fullResultPairList = new ArrayList<TestResultPair>();
	for (final String filename : fileNames) {
	    fullResultPairList.addAll(newTestResultPairList(path + "//" + filename));
	}

	final Collection<Object[]> testResultPairs = new ArrayList<Object[]>();
	for (final TestResultPair p : fullResultPairList) {
	    testResultPairs.add(new Object[] { p });
	}
	return testResultPairs;
    }

    public static List<TestResultPair> newTestResultPairList(
	    final String filename) throws IOException {
	final List<TestResultPair> list = new ArrayList<TestResultPair>();
	final URL fileUrl = AbstractMarkupFileTester.class
		.getResource(filename);
	final FileReader file = new FileReader(fileUrl.getFile());
	BufferedReader in = null;
	try {
	    in = new BufferedReader(file);
	    StringBuffer test = null;
	    StringBuffer result = null;

	    final Pattern pTest = Pattern.compile("# Test (\\w+) \\((.*)\\)");
	    final Pattern pResult = Pattern.compile("# Result (\\w+)");
	    String line;
	    int lineNumber = 0;

	    String testNumber = null;
	    String testName = null;
	    StringBuffer curbuf = null;
	    boolean ignored = false;
	    while ((line = in.readLine()) != null) {
		if (line.endsWith("-IGNORED")) {
		    ignored = true;
		    continue;
		}

		lineNumber++;
		final Matcher mTest = pTest.matcher(line);
		final Matcher mResult = pResult.matcher(line);

		if (mTest.matches()) { // # Test
		    if (ignored && !line.endsWith("-IGNORED")) {
			ignored = false;
		    }

		    testNumber = mTest.group(1);
		    testName = mTest.group(2);

		    addTestResultPair(list, test, result, testNumber, testName);

		    test = new StringBuffer();
		    result = new StringBuffer();
		    curbuf = test;
		} else if (mResult.matches()) { // # Result
		    if (ignored) {
			continue;
		    }

		    if (testNumber == null) {
			throw new RuntimeException(
				"Test file has result without a test (line "
					+ lineNumber + ")");
		    }
		    final String resultNumber = mResult.group(1);
		    if (!testNumber.equals(resultNumber)) {
			throw new RuntimeException(
				"Result " + resultNumber + " test " + testNumber
					+ " (line " + lineNumber + ")");
		    }

		    curbuf = result;
		} else {
		    if (ignored) {
			continue;
		    }

		    curbuf.append(line);
		    curbuf.append("\n");
		}
	    }

	    addTestResultPair(list, test, result, testNumber, testName);

	    return list;

	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (final IOException e) {
		}
	    }
	}
    }

    private static void addTestResultPair(final List<TestResultPair> list,
	    final StringBuffer testbuf, final StringBuffer resultbuf,
	    final String testNumber, final String testName) {
	if (testbuf == null || resultbuf == null) {
	    return;
	}

	final String test = chomp(testbuf.toString());
	final String result = chomp(resultbuf.toString());

	final String id = testNumber + "(" + testName + ")";

	list.add(new TestResultPair(id, test, result));
    }

    private static String chomp(final String s) {
	int lastPos = s.length() - 1;
	while (s.charAt(lastPos) == '\n' || s.charAt(lastPos) == '\r') {
	    lastPos--;
	}
	return s.substring(0, lastPos + 1);
    }
}
