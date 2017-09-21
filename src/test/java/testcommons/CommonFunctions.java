package testcommons;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import java.util.function.Function;

import mealplaner.BundleStore;

public class CommonFunctions {
	public static <T extends Enum<T>> void allEnumValuesHaveACorrespondingStringRepresentation(
			T[] enumValues,
			Function<BundleStore, EnumMap<T, String>> getEnumStrings) {
		BundleStore bundles = mock(BundleStore.class);
		when(bundles.message(anyString())).thenReturn("");

		EnumMap<T, String> enumMap = getEnumStrings.apply(bundles);
		for (T setting : enumValues) {
			assertTrue(enumMap.containsKey(setting));
		}
	}
}
