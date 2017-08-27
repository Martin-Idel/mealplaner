package integrationtests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
	private static final Pattern GET_STRING_AT_FILE_END = Pattern.compile("\\.getString\\($");
	private static final Pattern GET_STRING_PATTERN = Pattern.compile("\\.getString\\(.*?\\)");
	private static final Pattern STRING_PATTERN = Pattern.compile("\".*?\"");

	@Test
	public void allLinesInResourceBundleAreMatchedInJavaFiles() {
		try {
			List<String> keys = getKeysFromResourceBundle();
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

	// TODO: This is not enough: We can have calls elsewhere. In fact, we need
	// to either disallow calls where the string is not there immediately (bad),
	// or we need to check all strings. Or whatever. If you want to be sure, you
	// HAVE to forbid calls of the form getString(parameter), since you cannot
	// possibly track them.
	@Test
	public void allCallsInJavaFilesAreMatchedInResourceBundle() {
		try {
			List<String> keys = getKeysFromResourceBundle();
			List<Pair<Path, String>> potentialMatches = Files
					.find(Paths.get("src/main/java/"), 1000, (p, bfa) -> bfa.isRegularFile())
					.flatMap(this::findCallsToResourceBundle)
					.flatMap(pair -> findAllCallsToMissingKey(pair, keys))
					.collect(Collectors.toList());
			assertEmpty(potentialMatches);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/*
	 * This tests enforces that we never have a scenario where the string of the
	 * call to the ResourceBundle is matched on the new line. While I consider
	 * it good style anyways, this is also crucial to have a complete check for
	 * calls to ResourceBundles, since I parse line-wise.
	 */
	@Test
	public void noCallsToResourceBundleAreLineBroken() {
		try {
			List<Pair<Path, String>> filesWithErrors = Files
					.find(Paths.get("src/main/java/"), 1000, (p, bfa) -> bfa.isRegularFile())
					.flatMap(this::findWronglyFormattedLines)
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

	private Stream<Pair<Path, String>> findWronglyFormattedLines(Path path) {
		return readAllLines(path).stream()
				.filter(line -> GET_STRING_AT_FILE_END.matcher(line.trim()).find())
				.map(content -> Pair.of(path, content));
	}

	private Stream<Pair<Path, String>> findCallsToResourceBundle(Path path) {
		return readAllLines(path).stream()
				.filter(line -> GET_STRING_PATTERN.matcher(line).find())
				.map(string -> Pair.of(path, string));
	}

	private Stream<Pair<Path, String>> findAllCallsToMissingKey(Pair<Path, String> line,
			List<String> keys) {
		return extractKeys(line.right).stream()
				.filter(key -> !keys.contains(key))
				.map(string -> Pair.of(line.left, string));
	}

	private List<String> extractKeys(String line) {
		Matcher matcher = GET_STRING_PATTERN.matcher(line);
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
		} catch (Exception e) {
			fail("Parsing of file " + path + " failed");
			return new ArrayList<>();
		}
	}

	private List<String> getKeysFromResourceBundle() {
		ResourceBundle bundle = loadResourceBundle();
		Enumeration<String> keys = bundle.getKeys();
		List<String> keyList = new ArrayList<>();
		while (keys.hasMoreElements()) {
			keyList.add(keys.nextElement());
		}
		return keyList;
	}

	private ResourceBundle loadResourceBundle() {
		return ResourceBundle.getBundle("MessagesBundle", new ResourceBundle.Control() {
			@Override
			public List<Locale> getCandidateLocales(String name,
					Locale locale) {
				return Collections.singletonList(Locale.ROOT);
			}
		});
	}

	private static void assertEmptyKeyList(List<String> strings) {
		if (!strings.isEmpty()) {
			System.out.println(strings);
		}
		assertTrue(strings.isEmpty());
	}

	private static void assertEmpty(List<Pair<Path, String>> filesWithErrors) {
		if (!filesWithErrors.isEmpty()) {
			filesWithErrors.forEach(file -> System.out.println(file.left + " " +
					file.right));
		}
		assertTrue(filesWithErrors.isEmpty());
	}
}
