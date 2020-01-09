// SPDX-License-Identifier: MIT

package integrationtests;

import static bundletests.BundleCommons.GET_ERROR_AT_FILE_END;
import static bundletests.BundleCommons.GET_ERROR_PATTERN;
import static bundletests.BundleCommons.GET_ERROR_WITHOUT_QUOTE_PATTERN;
import static bundletests.BundleCommons.GET_STRING_AT_FILE_END;
import static bundletests.BundleCommons.GET_STRING_PATTERN;
import static bundletests.BundleCommons.GET_STRING_WITHOUT_QUOTE_PATTERN;
import static bundletests.BundleCommons.allCallsInJavaFilesAreMatchedInResourceBundle;
import static bundletests.BundleCommons.allLinesInResourceBundleAreMatchedInJavaFiles;
import static bundletests.BundleCommons.noCallsToResourceBundleAreLineBroken;
import static bundletests.BundleCommons.noCallsToResourceBundleUseStringParameters;

import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import bundletests.BundleCommons;

public class MessageBundleTest {
  @ParameterizedTest
  @MethodSource("bundles")
  public void allLinesInMessageBundleAreMatchedInJavaFiles(String bundle, Locale locale) {
    allLinesInResourceBundleAreMatchedInJavaFiles(bundle, locale, BundleCommons::allJavaFiles);
  }

  @ParameterizedTest
  @MethodSource("bundlesPatterns")
  public void allCallsInJavaFilesAreMatchedInMessageBundle(String bundle, Locale locale, Pattern pattern) {
    allCallsInJavaFilesAreMatchedInResourceBundle(bundle, locale, pattern, BundleCommons::allJavaFiles);
  }

  @Test
  public void noCallsToMessageBundleAreLineBroken() {
    noCallsToResourceBundleAreLineBroken(GET_STRING_AT_FILE_END, BundleCommons::allJavaFiles);
  }

  @Test
  public void noCallsToMessageBundleUseStringParameters() {
    noCallsToResourceBundleUseStringParameters(
        GET_STRING_WITHOUT_QUOTE_PATTERN, BundleCommons::allJavaFiles);
  }

  @Test
  public void noCallsToErrorBundleAreLineBroken() {
    noCallsToResourceBundleAreLineBroken(GET_ERROR_AT_FILE_END, BundleCommons::allJavaFiles);
  }

  @Test
  public void noCallsToErrorBundleUseStringParameters() {
    noCallsToResourceBundleUseStringParameters(
        GET_ERROR_WITHOUT_QUOTE_PATTERN, BundleCommons::allJavaFiles);
  }

  private static Stream<Arguments> bundles() {
    return Stream.of(
        Arguments.of("MessagesBundle", Locale.ROOT),
        Arguments.of("ErrorBundle", Locale.ROOT),
        Arguments.of("MessagesBundle", Locale.UK),
        Arguments.of("ErrorBundle", Locale.UK),
        Arguments.of("MessagesBundle", Locale.GERMAN),
        Arguments.of("ErrorBundle", Locale.GERMAN)
    );
  }

  private static Stream<Arguments> bundlesPatterns() {
    return Stream.of(
        Arguments.of("MessagesBundle", Locale.ROOT, GET_STRING_PATTERN),
        Arguments.of("ErrorBundle", Locale.ROOT, GET_ERROR_PATTERN),
        Arguments.of("MessagesBundle", Locale.UK, GET_STRING_PATTERN),
        Arguments.of("ErrorBundle", Locale.UK, GET_ERROR_PATTERN),
        Arguments.of("MessagesBundle", Locale.GERMAN, GET_STRING_PATTERN),
        Arguments.of("ErrorBundle", Locale.GERMAN, GET_ERROR_PATTERN)
    );
  }
}
