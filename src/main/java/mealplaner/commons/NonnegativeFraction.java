package mealplaner.commons;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NonnegativeFraction {
  public static final NonnegativeFraction ZERO = fraction(0, 1);

  private static final Pattern INTEGER_PATTERN = compile("^\\d*$");
  private static final Pattern FRACTION_PATTERN = compile("^\\d*[\\,\\/\\.]?\\d*$");
  private static final int[] POWERS_OF_10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000,
      100000000, 1000000000 };

  private final int numerator;
  private final int denominator;

  private NonnegativeFraction(int numerator, int denominator) {
    int greatestCommonDenominator = gcd(numerator, denominator);
    this.numerator = numerator / greatestCommonDenominator;
    this.denominator = denominator / greatestCommonDenominator;
  }

  private static int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
  }

  public static NonnegativeFraction wholeNumber(NonnegativeInteger integer) {
    return new NonnegativeFraction(integer.value, 1);
  }

  public static NonnegativeFraction fraction(int numerator, int denominator) {
    if (numerator * denominator < 0 || denominator == 0) {
      throw new NumberFormatException("Fraction must be nonnegative");
    }
    return new NonnegativeFraction(abs(numerator), abs(denominator));
  }

  public static NonnegativeFraction parse(String string) {
    String noWhiteSpaceString = string.replaceAll("\\s+", "");
    if ("".equals(noWhiteSpaceString)) {
      return ZERO;
    }
    Matcher integerMatcher = INTEGER_PATTERN.matcher(noWhiteSpaceString);
    if (integerMatcher.find()) {
      return fraction(Integer.parseInt(noWhiteSpaceString), 1);
    }
    Matcher fractionMatcher = FRACTION_PATTERN.matcher(noWhiteSpaceString);
    if (fractionMatcher.find()) {
      String matchedString = fractionMatcher.group();
      return matchedString.contains("/")
          ? parseFractionString(matchedString)
          : parseDecimalString(matchedString, matchedString.contains(",") ? "," : ".");
    }
    throw new NumberFormatException("String " + string + " cannot be parsed correctly.");
  }

  private static NonnegativeFraction parseFractionString(String matchedString) {
    String numeratorString = matchedString.substring(0, matchedString.indexOf("/"));
    String denominatorString = matchedString.substring(matchedString.indexOf("/") + 1,
        matchedString.length());
    return fraction("".equals(numeratorString) ? 1 : parseInt(numeratorString),
        "".equals(denominatorString) ? 1 : parseInt(denominatorString));
  }

  private static NonnegativeFraction parseDecimalString(String matchedString, String separator) {
    if (matchedString.startsWith(separator)) {
      String decimalPlaces = matchedString.substring(1);
      return fraction(parseInt(decimalPlaces), POWERS_OF_10[decimalPlaces.length()]);
    } else if (matchedString.endsWith(separator)) {
      return fraction(parseInt(matchedString.substring(0, matchedString.length() - 1)), 1);
    }
    String beforeSeparator = matchedString.substring(0, matchedString.indexOf(separator));
    String afterSeparator = matchedString.substring(matchedString.indexOf(separator) + 1,
        matchedString.length());
    return fraction(parseInt(beforeSeparator) * POWERS_OF_10[afterSeparator.length()]
        + parseInt(afterSeparator), POWERS_OF_10[afterSeparator.length()]);
  }

  public NonnegativeFraction add(NonnegativeFraction summand) {
    return new NonnegativeFraction(
        numerator * summand.denominator + summand.numerator * denominator,
        denominator * summand.denominator);
  }

  public NonnegativeFraction multiplyBy(NonnegativeInteger multiplier) {
    return fraction(numerator * multiplier.value, denominator);
  }

  public NonnegativeFraction divideBy(NonnegativeInteger divisor) {
    return fraction(numerator, denominator * divisor.value);
  }

  public int getNumerator() {
    return numerator;
  }

  public int getDenominator() {
    return denominator;
  }

  @Override
  public String toString() {
    if (denominator == 1) {
      return "" + numerator;
    }
    return "" + numerator + "/" + denominator;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + denominator;
    result = prime * result + numerator;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    NonnegativeFraction other = (NonnegativeFraction) obj;
    return this.numerator == other.numerator
        && this.denominator == other.denominator;
  }
}
