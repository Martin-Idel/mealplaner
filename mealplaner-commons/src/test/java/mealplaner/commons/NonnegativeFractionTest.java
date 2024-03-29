// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.parse;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class NonnegativeFractionTest {
  @Test
  void nonnegativeFractionsGetStoredCorrectly() {
    NonnegativeFraction fraction = wholeNumber(nonNegative(4));

    assertThat(fraction.getNumerator()).isEqualTo(4);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  void fractionsGetReducedCorrectly() {
    NonnegativeFraction fraction = fraction(10, 5);

    assertThat(fraction.getNumerator()).isEqualTo(2);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  void parseThrowsOnUnparseableString() {
    assertThrows(NumberFormatException.class, () -> parse("not_correct"));
  }

  @Test
  void negativeFractionThrowsException() {
    assertThrows(NumberFormatException.class, () -> fraction(10, -4));
  }

  @Test
  void zeroDenominatorThrowsException() {
    assertThrows(NumberFormatException.class, () -> fraction(10, 0));
  }

  @Test
  void positiveFractionWithNegativeSignsGetsSignsCorrected() {
    NonnegativeFraction fraction = fraction(-6, -4);

    assertThat(fraction.getNumerator()).isEqualTo(3);
    assertThat(fraction.getDenominator()).isEqualTo(2);
  }

  @Test
  void toRoundedStringRoundsUpIfTheNumberIsHigh() {
    NonnegativeFraction fraction = fraction(16, 3);

    assertThat(fraction.toRoundedString()).hasToString("6");
  }

  @Test
  void toRoundedStringRoundsUpDenominatorGetsTooLarge() {
    NonnegativeFraction fraction = fraction(33, 7);

    assertThat(fraction.toRoundedString()).hasToString("5");
  }

  @Test
  void toRoundedStringRevertsToNormalToStringForSmallNumbers() {
    NonnegativeFraction fraction = fraction(2, 3);

    assertThat(fraction.toString()).hasToString("2/3");
  }

  @Test
  void toRoundedStringReturnsCorrectNumberForWholeNumber() {
    NonnegativeFraction fraction = fraction(12, 3);

    assertThat(fraction.toString()).hasToString("4");
  }

  @Test
  void toStringDoesNotShowDenominatorIfOne() {
    NonnegativeFraction fraction = fraction(3, 1);

    assertThat(fraction.toString()).hasToString("3");
  }

  @Test
  void toStringShowsDenominatorForOtherFractions() {
    NonnegativeFraction fraction = fraction(1, 2);

    assertThat(fraction.toString()).hasToString("1/2");
  }

  @Test
  void parseIdentifiesEmptyStringAsZero() {
    NonnegativeFraction fraction = parse(" ");

    assertThat(fraction).isEqualTo(NonnegativeFraction.ZERO);
  }

  @Test
  void parseIdentifiesIntegers() {
    NonnegativeFraction fraction = parse("4");

    assertThat(fraction.getNumerator()).isEqualTo(4);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  void parseIdentifiesFractionsSeparatedBySlash() {
    NonnegativeFraction fraction = parse("1/2");

    assertThat(fraction.getNumerator()).isEqualTo(1);
    assertThat(fraction.getDenominator()).isEqualTo(2);
  }

  @Test
  void parseIdentifiesFractionsSeparatedByComma() {
    NonnegativeFraction fraction = parse("4,3");

    assertThat(fraction.getNumerator()).isEqualTo(43);
    assertThat(fraction.getDenominator()).isEqualTo(10);
  }

  @Test
  void parseIdentifiesFractionsSeparatedByDot() {
    NonnegativeFraction fraction = parse("4.3");

    assertThat(fraction.getNumerator()).isEqualTo(43);
    assertThat(fraction.getDenominator()).isEqualTo(10);
  }

  @Test
  void parseIdentifiesFractionsIncludingWhitespace() {
    NonnegativeFraction fraction = parse("4 /3 ");

    assertThat(fraction.getNumerator()).isEqualTo(4);
    assertThat(fraction.getDenominator()).isEqualTo(3);
  }

  @Test
  void parseIdentifiesFractionsWithStartingDot() {
    NonnegativeFraction fraction = parse(".3");

    assertThat(fraction.getNumerator()).isEqualTo(3);
    assertThat(fraction.getDenominator()).isEqualTo(10);
  }

  @Test
  void parseIdentifiesFractionsWithTrailingDot() {
    NonnegativeFraction fraction = parse("3.");

    assertThat(fraction.getNumerator()).isEqualTo(3);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  void parseIdentifiesFractionsWithStartingSlash() {
    NonnegativeFraction fraction = parse("/3");

    assertThat(fraction.getNumerator()).isZero();
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  void parseIdentifiesFractionsWithTrailingSlash() {
    NonnegativeFraction fraction = parse("3/");

    assertThat(fraction.getNumerator()).isEqualTo(3);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  void plusCorrectlyAddsFractions() {
    NonnegativeFraction fraction = fraction(6, 4);
    NonnegativeFraction secondFraction = fraction(2, 6);

    NonnegativeFraction addedFractions = fraction.add(secondFraction);

    assertThat(addedFractions.getNumerator()).isEqualTo(11);
    assertThat(addedFractions.getDenominator()).isEqualTo(6);
  }

  @Test
  void roundTripParsingWorksCorrectly() {
    NonnegativeFraction fraction = fraction(10, 3);

    NonnegativeFraction roundtripFraction = parse(fraction.toString());

    assertThat(roundtripFraction).isEqualTo(fraction);
  }

  @Test
  void multiplyByCorrectlyMultipliesFraction() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction multipliedFraction = fraction.multiplyBy(TWO);

    assertThat(multipliedFraction.getNumerator()).isEqualTo(10);
    assertThat(multipliedFraction.getDenominator()).isEqualTo(3);
  }

  @Test
  void divideByCorrectlyMultipliesFraction() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction multipliedFraction = fraction.divideBy(TWO);

    assertThat(multipliedFraction.getNumerator()).isEqualTo(5);
    assertThat(multipliedFraction.getDenominator()).isEqualTo(6);
  }

  @Test
  void multiplyByFractionCorrectlyMultipliesTwoFractions() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction multipliedFraction = fraction.multiplyBy(fraction(3, 5));

    assertThat(multipliedFraction.getNumerator()).isEqualTo(1);
    assertThat(multipliedFraction.getDenominator()).isEqualTo(1);
  }

  @Test
  void invertInvertsFraction() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction invertedFraction = fraction.invert();

    assertThat(invertedFraction.getNumerator()).isEqualTo(3);
    assertThat(invertedFraction.getDenominator()).isEqualTo(5);
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(NonnegativeFraction.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
