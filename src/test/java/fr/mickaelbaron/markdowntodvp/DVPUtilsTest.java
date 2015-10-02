package fr.mickaelbaron.markdowntodvp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mickael BARON
 */
public class DVPUtilsTest {

    @Test
    public void getRomanNumeralsFromNumberTest() {
	Assert.assertEquals("I", DVPUtils.getRomanNumeralsFromNumber(1));
	Assert.assertEquals("X", DVPUtils.getRomanNumeralsFromNumber(10));
    }
    
    @Test
    public void replaceIntoImageDirectoryTest() {	
	String replaceIntoImageDirectory = DVPUtils.replaceIntoImageDirectory("assets/neo4j-logo.png");
	Assert.assertNotNull(replaceIntoImageDirectory);
	Assert.assertEquals("images/neo4j-logo.png", replaceIntoImageDirectory);
	
	replaceIntoImageDirectory = DVPUtils.replaceIntoImageDirectory("assets/first1/first2/first3/neo4j-logo.png");
	Assert.assertNotNull(replaceIntoImageDirectory);
	Assert.assertEquals("images/neo4j-logo.png", replaceIntoImageDirectory);
	
	replaceIntoImageDirectory = DVPUtils.replaceIntoImageDirectory("diag-c604b0fabaeb8e8d7c713a14e35a9392.png");
	Assert.assertNotNull(replaceIntoImageDirectory);
	Assert.assertEquals("images/diag-c604b0fabaeb8e8d7c713a14e35a9392.png", replaceIntoImageDirectory);
    }
}
