package fr.mickaelbaron.markdowntodvp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mickael BARON
 */
public class DVPDecoratorTest {
    
    @Test
    public void displayLevelTest() {
	DVPDecorator currentDVP = new DVPDecorator();
	Assert.assertEquals("II", currentDVP.displayLevel(0, 2));
	Assert.assertEquals("b", currentDVP.displayLevel(1, 2));
    }
    
    @Test
    public void openHeadlineTest() {
	DVPDecorator currentDVP = new DVPDecorator();
	StringBuilder cb = new StringBuilder();
	currentDVP.openHeadline(cb, 1);
	Assert.assertEquals("<section id=\"I\">\n<title>", cb.toString());
	
	cb = new StringBuilder();
	currentDVP.openHeadline(cb, 2);
	Assert.assertEquals("<section id=\"I.a\">\n<title>", cb.toString());
    }
}
