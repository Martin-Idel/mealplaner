// SPDX-License-Identifier: MIT

package bundletests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mealplaner.commons.Pair;

public class BundleCommons {
  public static final Pattern GET_STRING_AT_FILE_END = Pattern.compile("\\.message\\($");
  public static final Pattern GET_STRING_PATTERN = Pattern.compile("\\.message\\(.*?\\)");
  public static final Pattern GET_STRING_WITHOUT_QUOTE_PATTERN = Pattern
      .compile("\\.message\\([^\"]");

  public static final Pattern GET_ERROR_AT_FILE_END = Pattern.compile("\\.errorMessage\\($");
  public static final Pattern GET_ERROR_PATTERN = Pattern.compile("\\.errorMessage\\(.*?\\)");
  public static final Pattern GET_ERROR_WITHOUT_QUOTE_PATTERN = Pattern
      .compile("\\.errorMessage\\([^\"]");

  private static final Pattern STRING_PATTERN = Pattern.compile("\".*?\"");

  public static void allMessageTests(String bundle, String plugin) {
    MessageBundleRepresentation messageBundleRepresentation = new MessageBundleRepresentation();
    assertThat(allLinesInResourceBundleAreMatchedInJavaFiles(bundle, Locale.ROOT, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
    assertThat(allCallsInJavaFilesAreMatchedInResourceBundle(
        bundle, Locale.ROOT, GET_STRING_PATTERN, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
    assertThat(allLinesInResourceBundleAreMatchedInJavaFiles(
        bundle, Locale.GERMAN, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
    assertThat(allCallsInJavaFilesAreMatchedInResourceBundle(
        bundle, Locale.GERMAN, GET_STRING_PATTERN, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
    assertThat(allLinesInResourceBundleAreMatchedInJavaFiles(
        bundle, Locale.UK, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
    assertThat(allCallsInJavaFilesAreMatchedInResourceBundle(
        bundle, Locale.UK, GET_STRING_PATTERN, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
    assertThat(noCallsToResourceBundleAreLineBroken(GET_STRING_AT_FILE_END, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
    assertThat(noCallsToResourceBundleUseStringParameters(
        GET_STRING_WITHOUT_QUOTE_PATTERN, () -> pluginJavaFiles(plugin)))
        .withRepresentation(messageBundleRepresentation).isEmpty();
  }

  public static List<String> allLinesInResourceBundleAreMatchedInJavaFiles(
      String resourceBundleName, Locale locale, Supplier<Stream<Path>> files) {
    List<String> keys = getKeysFromResourceBundle(resourceBundleName, locale);
    List<String> allLines = files.get()
        .flatMap(path -> readAllLines(path).stream())
        .collect(Collectors.toList());
    return keys.stream()
        .filter(key -> !keyIsContainedIn(key, allLines))
        .collect(Collectors.toList());
  }

  public static List<Pair<Path, String>> allCallsInJavaFilesAreMatchedInResourceBundle(
      String bundleName, Locale locale, final Pattern bundleCalls, Supplier<Stream<Path>> files) {
    List<String> keys = getKeysFromResourceBundle(bundleName, locale);
    return files.get()
        .flatMap(path -> findCallsToResourceBundle(path, bundleCalls))
        .flatMap(pair -> findAllCallsToMissingKey(pair, keys, bundleCalls))
        .collect(Collectors.toList());
  }

  /*
   * This test enforces that we never have a scenario where the string of the call
   * to the BundleStore is matched on the new line. While I consider it good style
   * anyways, this is also crucial to have a more complete check for calls to
   * ResourceBundles, since I parse line-wise.
   */
  public static List<Pair<Path, String>> noCallsToResourceBundleAreLineBroken(
      final Pattern bundleCallAtEndOfLine, Supplier<Stream<Path>> files) {
    return files.get()
        .flatMap(path -> findWronglyFormattedLines(path, bundleCallAtEndOfLine))
        .collect(Collectors.toList());
  }

  /*
   * This test enforces that we never have a scenario where the string of a call
   * to the BundleStore is passed as a parameter. This is a necessary to be able
   * to detect all calls to the BundleStore.
   */
  public static List<Pair<Path, String>> noCallsToResourceBundleUseStringParameters(
      final Pattern parameterCall, Supplier<Stream<Path>> files) {
    return files.get()
        .flatMap(path -> findLinesWithParameterCalls(path, parameterCall))
        .collect(Collectors.toList());
  }

  private static boolean keyIsContainedIn(String key, List<String> lines) {
    return lines.stream().anyMatch(line -> line.contains("\"" + key + "\""));
  }

  private static Stream<Pair<Path, String>> findLinesWithParameterCalls(
      Path path, Pattern parameterCallPattern) {
    return readAllLines(path).stream()
        .filter(line -> parameterCallPattern.matcher(line.trim()).find())
        .map(content -> Pair.of(path, content));
  }

  private static Stream<Pair<Path, String>> findWronglyFormattedLines(
      Path path, Pattern fileEndPattern) {
    return readAllLines(path).stream()
        .filter(line -> fileEndPattern.matcher(line.trim()).find())
        .map(content -> Pair.of(path, content));
  }

  private static Stream<Pair<Path, String>> findCallsToResourceBundle(Path path, Pattern callPattern) {
    return readAllLines(path).stream()
        .filter(line -> callPattern.matcher(line).find())
        .map(string -> Pair.of(path, string));
  }

  private static Stream<Pair<Path, String>> findAllCallsToMissingKey(
      Pair<Path, String> line, List<String> keys, final Pattern bundleCall) {
    return extractKeys(line.right, bundleCall).stream()
        .filter(key -> !keys.contains(key))
        .map(string -> Pair.of(line.left, string));
  }

  private static List<String> extractKeys(String line, Pattern bundleCall) {
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

  private static List<String> readAllLines(Path path) {
    try {
      return Files.readAllLines(path);
    } catch (Exception e) { // NOPMD
      fail("Parsing of file " + path + " failed");
      return new ArrayList<>();
    }
  }

  private static List<String> getKeysFromResourceBundle(String bundleName, Locale locale) {
    ResourceBundle bundle = loadResourceBundle(bundleName, locale);
    Enumeration<String> keys = bundle.getKeys();
    List<String> keyList = new ArrayList<>();
    while (keys.hasMoreElements()) {
      keyList.add(keys.nextElement());
    }
    return keyList;
  }

  private static ResourceBundle loadResourceBundle(String bundleName, Locale locale) {
    return ResourceBundle.getBundle(bundleName, locale);
  }

  private static void assertEmptyKeyList(List<String> strings) {
    if (!strings.isEmpty()) {
      System.out.println("Size: " + strings.size());
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

  public static Stream<Path> allJavaFiles() {
    try {
      return Files.find(Paths.get(".."), 999,
          (p, bfa) -> bfa.isRegularFile()
              && p.getFileName().toString().endsWith(".java")
              && !p.toString().contains("/test/")
              && !p.toString().contains("/mealplaner-plugins/")
      );
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
      return Stream.empty();
    }
  }

  public static Stream<Path> pluginJavaFiles(String pluginName) {
    try {
      return Files.find(Paths.get(".."), 999,
          (p, bfa) -> bfa.isRegularFile()
              && p.getFileName().toString().endsWith(".java")
              && p.toString().contains(pluginName));
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
      return Stream.empty();
    }
  }
}
