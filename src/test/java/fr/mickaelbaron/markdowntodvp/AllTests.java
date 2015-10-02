package fr.mickaelbaron.markdowntodvp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.mickaelbaron.markdowntodvp.functional.AllFunctionalTests;

/**
 * @author Mickael BARON
 */
@RunWith(Suite.class)
@SuiteClasses(value = {
	DVPDecoratorTest.class,
	DVPUtilsTest.class,
	M2DVPRunTest.class,
	AllFunctionalTests.class}
	)
public class AllTests {
}
