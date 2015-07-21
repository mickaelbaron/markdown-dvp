package fr.mickaelbaron.markdowntodvp.functional;

/**
 * @author Mickael BARON
 */
public class TestResultPair {
    
    private String name;
    
    private String test;
    
    private String result;

    public TestResultPair(String name, String test, String result) {
	this.name = name;
	this.test = test;
	this.result = result;
    }

    public String getTest() {
	return test;
    }

    public String getResult() {
	return result;
    }

    @Override
    public String toString() {
	return name;
    }
}
