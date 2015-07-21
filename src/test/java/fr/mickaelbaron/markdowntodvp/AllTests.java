package fr.mickaelbaron.markdowntodvp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mickael BARON
 */
@RunWith(Suite.class)
@SuiteClasses(value = {
	DVPDecoratorTest.class,
	DVPUtils.class,
	M2DVPRun.class}
	)
public class AllTests {
}
