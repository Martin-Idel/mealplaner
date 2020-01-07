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
import org.junit.jupiter.params.provider.ValueSource;

import bundletests.BundleCommons;

public class MessageBundleTest {
  @ParameterizedTest
  @ValueSource(strings = {"MessagesBundle", "ErrorBundle"})
  public void allLinesInMessageBundleAreMatchedInJavaFiles(String bundle) {
    allLinesInResourceBundleAreMatchedInJavaFiles(bundle, Locale.ROOT, BundleCommons::allJavaFiles);
  }

  @ParameterizedTest
  @MethodSource("bundles")
  public void allCallsInJavaFilesAreMatchedInMessageBundle(String bundle, Pattern pattern) {
    allCallsInJavaFilesAreMatchedInResourceBundle(
        bundle, Locale.ROOT, pattern, BundleCommons::allJavaFiles);
  }

  @ParameterizedTest
  @ValueSource(strings = {"MessagesBundle", "ErrorBundle"})
  public void allLinesInMessageBundleAreMatchedInJavaFilesGerman(String bundle) {
    allLinesInResourceBundleAreMatchedInJavaFiles(
        bundle, Locale.GERMAN, BundleCommons::allJavaFiles);
  }

  @ParameterizedTest
  @MethodSource("bundles")
  public void allCallsInJavaFilesAreMatchedInMessageBundleGerman(String bundle, Pattern pattern) {
    allCallsInJavaFilesAreMatchedInResourceBundle(
        bundle, Locale.GERMAN, pattern, BundleCommons::allJavaFiles);
  }

  @ParameterizedTest
  @ValueSource(strings = {"MessagesBundle", "ErrorBundle"})
  public void allLinesInMessageBundleAreMatchedInJavaFilesEnglish(String bundle) {
    allLinesInResourceBundleAreMatchedInJavaFiles(
        bundle, Locale.UK, BundleCommons::allJavaFiles);
  }

  @ParameterizedTest
  @MethodSource("bundles")
  public void allCallsInJavaFilesAreMatchedInMessageBundleEnglish(String bundle, Pattern pattern) {
    allCallsInJavaFilesAreMatchedInResourceBundle(
        bundle, Locale.UK, pattern, BundleCommons::allJavaFiles);
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
        Arguments.of("MessagesBundle", GET_STRING_PATTERN),
        Arguments.of("ErrorBundle", GET_ERROR_PATTERN));
  }
}
