package mealplaner.commons;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.parse;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
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

    NonnegativeFraction addedFractions = fraction.plus(secondFraction);

    assertThat(addedFractions.getNumerator()).isEqualTo(11);
    assertThat(addedFractions.getDenominator()).isEqualTo(6);
  }

  @Test
  public void minusSubstractsSmallerFractions() {
    NonnegativeFraction fraction = fraction(6, 4);
    NonnegativeFraction secondFraction = fraction(2, 6);

    NonnegativeFraction substractedFraction = fraction.minus(secondFraction);

    assertThat(substractedFraction.getNumerator()).isEqualTo(7);
    assertThat(substractedFraction.getDenominator()).isEqualTo(6);
  }

  @Test(expected = NumberFormatException.class)
  public void minusThrowsExceptionIfBiggerFractionGetsSubstracted() {
    NonnegativeFraction fraction = fraction(6, 4);
    NonnegativeFraction secondFraction = fraction(2, 1);

    NonnegativeFraction substractedFraction = fraction.minus(secondFraction);

    assertThat(substractedFraction.getNumerator()).isEqualTo(1);
  }
}
