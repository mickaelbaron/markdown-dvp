package fr.mickaelbaron.markdowntodvp.functional;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mickael BARON
 */
@RunWith(Suite.class)
@SuiteClasses(value = {
	MarkupFileTesterWithDocument.class,
	MarkupFileTesterWithoutDocument.class,
	MarkupFileTesterWithoutDocumentAndWithExtensions.class,
	MarkupFileTesterWithoutDocumentWithExtensionsAndForCodeBlockEmitter.class}
	)
public class AllFunctionalTests {
}
