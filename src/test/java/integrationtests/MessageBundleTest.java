// SPDX-License-Identifier: MIT

package integrationtests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import mealplaner.commons.Pair;

public class MessageBundleTest {
  private static final Pattern GET_STRING_AT_FILE_END = Pattern.compile("\\.message\\($");
  private static final Pattern GET_STRING_PATTERN = Pattern.compile("\\.message\\(.*?\\)");
  private static final Pattern GET_STRING_WITHOUT_QUOTE_PATTERN = Pattern
      .compile("\\.message\\([^\"]");

  private static final Pattern GET_ERROR_AT_FILE_END = Pattern.compile("\\.errorMessage\\($");
  private static final Pattern GET_ERROR_PATTERN = Pattern.compile("\\.errorMessage\\(.*?\\)");
  private static final Pattern GET_ERROR_WITHOUT_QUOTE_PATTERN = Pattern
      .compile("\\.errorMessage\\([^\"]");

  private static final Pattern STRING_PATTERN = Pattern.compile("\".*?\"");

  @Test
  public void allLinesInMessageBundleAreMatchedInJavaFiles() {
    allLinesInResourceBundleAreMatchedInJavaFiles("MessagesBundle", Locale.ROOT);
  }

  @Test
  public void allCallsInJavaFilesAreMatchedInMessageBundle() {
    allCallsInJavaFilesAreMatchedInResourceBundle("MessagesBundle", Locale.ROOT,
        GET_STRING_PATTERN);
  }

  @Test
  public void allLinesInMessageBundleAreMatchedInJavaFilesGerman() {
    allLinesInResourceBundleAreMatchedInJavaFiles("MessagesBundle", Locale.GERMAN);
  }

  @Test
  public void allCallsInJavaFilesAreMatchedInMessageBundleGerman() {
    allCallsInJavaFilesAreMatchedInResourceBundle("MessagesBundle", Locale.GERMAN,
        GET_STRING_PATTERN);
  }

  @Test
  public void allLinesInMessageBundleAreMatchedInJavaFilesEnglish() {
    allLinesInResourceBundleAreMatchedInJavaFiles("MessagesBundle", Locale.UK);
  }

  @Test
  public void allCallsInJavaFilesAreMatchedInMessageBundleEnglish() {
    allCallsInJavaFilesAreMatchedInResourceBundle("MessagesBundle", Locale.UK,
        GET_STRING_PATTERN);
  }

  @Test
  public void noCallsToMessageBundleAreLineBroken() {
    noCallsToResourceBundleAreLineBroken(GET_STRING_AT_FILE_END);
  }

  @Test
  public void noCallsToMessageBundleUseStringParameters() {
    noCallsToResourceBundleUseStringParameters(GET_STRING_WITHOUT_QUOTE_PATTERN);
  }

  @Test
  public void allLinesInErrorBundleAreMatchedInJavaFiles() {
    allLinesInResourceBundleAreMatchedInJavaFiles("ErrorBundle", Locale.ROOT);
  }

  @Test
  public void allCallsInJavaFilesAreMatchedInErrorBundle() {
    allCallsInJavaFilesAreMatchedInResourceBundle("ErrorBundle", Locale.ROOT,
        GET_ERROR_PATTERN);
  }

  @Test
  public void allLinesInErrorBundleAreMatchedInJavaFilesGerman() {
    allLinesInResourceBundleAreMatchedInJavaFiles("ErrorBundle", Locale.GERMAN);
  }

  @Test
  public void allCallsInJavaFilesAreMatchedInErrorBundleGerman() {
    allCallsInJavaFilesAreMatchedInResourceBundle("ErrorBundle", Locale.GERMAN,
        GET_ERROR_PATTERN);
  }

  @Test
  public void allLinesInErrorBundleAreMatchedInJavaFilesEnglish() {
    allLinesInResourceBundleAreMatchedInJavaFiles("ErrorBundle", Locale.US);
  }

  @Test
  public void allCallsInJavaFilesAreMatchedInErrorBundleEnglish() {
    allCallsInJavaFilesAreMatchedInResourceBundle("ErrorBundle", Locale.US,
        GET_ERROR_PATTERN);
  }

  @Test
  public void noCallsToErrorBundleAreLineBroken() {
    noCallsToResourceBundleAreLineBroken(GET_ERROR_AT_FILE_END);
  }

  @Test
  public void noCallsToErrorBundleUseStringParameters() {
    noCallsToResourceBundleUseStringParameters(GET_ERROR_WITHOUT_QUOTE_PATTERN);
  }

  private void allLinesInResourceBundleAreMatchedInJavaFiles(String resourceBundleName,
      Locale locale) {
    try {
      List<String> keys = getKeysFromResourceBundle(resourceBundleName, locale);
      List<String> allLines = Files
          .find(Paths.get("src/main/java/"), 999, (p, bfa) -> bfa.isRegularFile())
          .flatMap(path -> readAllLines(path).stream())
          .collect(Collectors.toList());
      List<String> potentialMatches = keys.stream()
          .filter(key -> !keyIsContainedIn(key, allLines))
          .collect(Collectors.toList());
      assertEmptyKeyList(potentialMatches);
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  private void allCallsInJavaFilesAreMatchedInResourceBundle(String bundleName, Locale locale,
      final Pattern bundleCalls) {
    try {
      List<String> keys = getKeysFromResourceBundle(bundleName, locale);
      List<Pair<Path, String>> potentialMatches = Files
          .find(Paths.get("src/main/java/"), 1000, (p, bfa) -> bfa.isRegularFile())
          .flatMap(path -> findCallsToResourceBundle(path, bundleCalls))
          .flatMap(pair -> findAllCallsToMissingKey(pair, keys, bundleCalls))
          .collect(Collectors.toList());
      assertEmpty(potentialMatches);
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  /*
   * This test enforces that we never have a scenario where the string of the call
   * to the BundleStore is matched on the new line. While I consider it good style
   * anyways, this is also crucial to have a more complete check for calls to
   * ResourceBundles, since I parse line-wise.
   */
  private void noCallsToResourceBundleAreLineBroken(final Pattern bundleCallAtEndOfLine) {
    try {
      List<Pair<Path, String>> filesWithErrors = Files
          .find(Paths.get("src/main/java/"), 1000, (p, bfa) -> bfa.isRegularFile())
          .flatMap(path -> findWronglyFormattedLines(path, bundleCallAtEndOfLine))
          .collect(Collectors.toList());
      assertEmpty(filesWithErrors);
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  /*
   * This test enforces that we never have a scenario where the string of a call
   * to the BundleStore is passed as a parameter. This is a necessary to be able
   * to detect all calls to the BundleStore.
   */
  private void noCallsToResourceBundleUseStringParameters(final Pattern parameterCall) {
    try {
      List<Pair<Path, String>> filesWithErrors = Files
          .find(Paths.get("src/main/java/"), 1000, (p, bfa) -> bfa.isRegularFile())
          .flatMap(path -> findLinesWithParameterCalls(path, parameterCall))
          .collect(Collectors.toList());
      assertEmpty(filesWithErrors);
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  private boolean keyIsContainedIn(String key, List<String> lines) {
    return lines.stream().anyMatch(line -> line.contains("\"" + key + "\""));
  }

  private Stream<Pair<Path, String>> findLinesWithParameterCalls(Path path,
      Pattern parameterCallPattern) {
    return readAllLines(path).stream()
        .filter(line -> parameterCallPattern.matcher(line.trim()).find())
        .map(content -> Pair.of(path, content));
  }

  private Stream<Pair<Path, String>> findWronglyFormattedLines(Path path,
      Pattern fileEndPattern) {
    return readAllLines(path).stream()
        .filter(line -> fileEndPattern.matcher(line.trim()).find())
        .map(content -> Pair.of(path, content));
  }

  private Stream<Pair<Path, String>> findCallsToResourceBundle(Path path, Pattern callPattern) {
    return readAllLines(path).stream()
        .filter(line -> callPattern.matcher(line).find())
        .map(string -> Pair.of(path, string));
  }

  private Stream<Pair<Path, String>> findAllCallsToMissingKey(Pair<Path, String> line,
      List<String> keys, final Pattern bundleCall) {
    return extractKeys(line.right, bundleCall).stream()
        .filter(key -> !keys.contains(key))
        .map(string -> Pair.of(line.left, string));
  }

  private List<String> extractKeys(String line, Pattern bundleCall) {
    Matcher matcher = bundleCall.matcher(line);
    List<String> matchedStrings = new ArrayList<>();
    while (matcher.find()) {
      Matcher keyMatcher = STRING_PATTERN.matcher(matcher.group(0));
      while (keyMatcher.find()) {
        matchedStrings.add(matcher.group(0)
            .substring(keyMatcher.start(0) + 1, keyMatcher.end(0) - 1));
      }
    }
    return matchedStrings;
  }

  private List<String> readAllLines(Path path) {
    try {
      return Files.readAllLines(path);
    } catch (Exception e) { // NOPMD
      fail("Parsing of file " + path + " failed");
      return new ArrayList<>();
    }
  }

  private List<String> getKeysFromResourceBundle(String bundleName, Locale locale) {
    ResourceBundle bundle = loadResourceBundle(bundleName, locale);
    Enumeration<String> keys = bundle.getKeys();
    List<String> keyList = new ArrayList<>();
    while (keys.hasMoreElements()) {
      keyList.add(keys.nextElement());
    }
    return keyList;
  }

  private ResourceBundle loadResourceBundle(String bundleName, Locale locale) {
    return ResourceBundle.getBundle(bundleName, locale);
  }

  private static void assertEmptyKeyList(List<String> strings) {
    if (!strings.isEmpty()) {
      System.out.println(strings);
    }
    assertTrue(strings.isEmpty());
  }

  private static void assertEmpty(List<Pair<Path, String>> filesWithErrors) {
    if (!filesWithErrors.isEmpty()) {
      filesWithErrors.forEach(file -> System.out.println(file.left + " "
          + file.right));
    }
    assertTrue(filesWithErrors.isEmpty());
  }
}
