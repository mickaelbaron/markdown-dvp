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
}
