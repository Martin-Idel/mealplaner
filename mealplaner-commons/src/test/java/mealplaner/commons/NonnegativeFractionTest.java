// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.parse;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NonnegativeFractionTest {
  @Test
  public void nonnegativeFractionsGetStoredCorrectly() {
    NonnegativeFraction fraction = wholeNumber(nonNegative(4));

    assertThat(fraction.getNumerator()).isEqualTo(4);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  public void fractionsGetReducedCorrectly() {
    NonnegativeFraction fraction = fraction(10, 5);

    assertThat(fraction.getNumerator()).isEqualTo(2);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test(expected = NumberFormatException.class)
  public void negativeFractionThrowsException() {
    NonnegativeFraction fraction = fraction(10, -4);

    assertThat(fraction.getDenominator()).isEqualTo(2);
  }

  @Test(expected = NumberFormatException.class)
  public void zeroDenominatorThrowsException() {
    NonnegativeFraction fraction = fraction(10, 0);

    assertThat(fraction.getDenominator()).isEqualTo(2);
  }

  @Test
  public void positiveFractionWithNegativeSignsGetsSignsCorrected() {
    NonnegativeFraction fraction = fraction(-6, -4);

    assertThat(fraction.getNumerator()).isEqualTo(3);
    assertThat(fraction.getDenominator()).isEqualTo(2);
  }

  @Test
  public void toStringDoesNotShowDenominatorIfOne() {
    NonnegativeFraction fraction = fraction(3, 1);

    assertThat(fraction.toString()).isEqualTo("3");
  }

  @Test
  public void toStringShowsDenominatorForOtherFractions() {
    NonnegativeFraction fraction = fraction(1, 2);

    assertThat(fraction.toString()).isEqualTo("1/2");
  }

  @Test
  public void parseIdentifiesIntegers() {
    NonnegativeFraction fraction = parse("4");

    assertThat(fraction.getNumerator()).isEqualTo(4);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  public void parseIdentifiesFractionsSeparatedBySlash() {
    NonnegativeFraction fraction = parse("1/2");

    assertThat(fraction.getNumerator()).isEqualTo(1);
    assertThat(fraction.getDenominator()).isEqualTo(2);
  }

  @Test
  public void parseIdentifiesFractionsSeparatedByComma() {
    NonnegativeFraction fraction = parse("4,3");

    assertThat(fraction.getNumerator()).isEqualTo(43);
    assertThat(fraction.getDenominator()).isEqualTo(10);
  }

  @Test
  public void parseIdentifiesFractionsSeparatedByDot() {
    NonnegativeFraction fraction = parse("4.3");

    assertThat(fraction.getNumerator()).isEqualTo(43);
    assertThat(fraction.getDenominator()).isEqualTo(10);
  }

  @Test
  public void parseIdentifiesFractionsIncludingWhitespace() {
    NonnegativeFraction fraction = parse("4 /3 ");

    assertThat(fraction.getNumerator()).isEqualTo(4);
    assertThat(fraction.getDenominator()).isEqualTo(3);
  }

  @Test
  public void parseIdentifiesFractionsWithStartingDot() {
    NonnegativeFraction fraction = parse(".3");

    assertThat(fraction.getNumerator()).isEqualTo(3);
    assertThat(fraction.getDenominator()).isEqualTo(10);
  }

  @Test
  public void parseIdentifiesFractionsWithTrailingDot() {
    NonnegativeFraction fraction = parse("3.");

    assertThat(fraction.getNumerator()).isEqualTo(3);
    assertThat(fraction.getDenominator()).isEqualTo(1);
  }

  @Test
  public void plusCorrectlyAddsFractions() {
    NonnegativeFraction fraction = fraction(6, 4);
    NonnegativeFraction secondFraction = fraction(2, 6);

    NonnegativeFraction addedFractions = fraction.add(secondFraction);

    assertThat(addedFractions.getNumerator()).isEqualTo(11);
    assertThat(addedFractions.getDenominator()).isEqualTo(6);
  }

  @Test
  public void roundTripParsingWorksCorrectly() {
    NonnegativeFraction fraction = fraction(10, 3);

    NonnegativeFraction roundtripFraction = parse(fraction.toString());

    assertThat(roundtripFraction).isEqualTo(fraction);
  }

  @Test
  public void multiplyByCorrectlyMultipliesFraction() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction multipliedFraction = fraction.multiplyBy(TWO);

    assertThat(multipliedFraction.getNumerator()).isEqualTo(10);
    assertThat(multipliedFraction.getDenominator()).isEqualTo(3);
  }

  @Test
  public void divideByCorrectlyMultipliesFraction() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction multipliedFraction = fraction.divideBy(TWO);

    assertThat(multipliedFraction.getNumerator()).isEqualTo(5);
    assertThat(multipliedFraction.getDenominator()).isEqualTo(6);
  }

  @Test
  public void multiplyByFractionCorrectlyMultipliesTwoFractions() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction multipliedFraction = fraction.multiplyBy(fraction(3, 5));

    assertThat(multipliedFraction.getNumerator()).isEqualTo(1);
    assertThat(multipliedFraction.getDenominator()).isEqualTo(1);
  }

  @Test
  public void invertInvertsFraction() {
    NonnegativeFraction fraction = fraction(5, 3);

    NonnegativeFraction invertedFraction = fraction.invert();

    assertThat(invertedFraction.getNumerator()).isEqualTo(3);
    assertThat(invertedFraction.getDenominator()).isEqualTo(5);
  }
}
