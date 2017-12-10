package testcommons;

import static java.util.Locale.getDefault;
import static java.util.ResourceBundle.getBundle;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.ResourceBundle;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class BundlesInitialization extends ExternalResource {

  @Override
  public Statement apply(final Statement base, final Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        ResourceBundle messages = getBundle("MessagesBundle", getDefault());
        ResourceBundle errors = getBundle("ErrorBundle", getDefault());
        BUNDLES.setMessageBundle(messages);
        BUNDLES.setErrorBundle(errors);
        BUNDLES.setLocale(getDefault());
        base.evaluate();
      }
    };
  }
}
