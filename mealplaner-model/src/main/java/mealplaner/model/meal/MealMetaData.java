// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.MealFact;

public final class MealMetaData {
  private static final MealMetaData EMPTY_METADATA = new MealMetaData(
      "EMPTY",
      new HashMap<>(),
      new ArrayList<>());

  private final String name;
  private final Map<Class, MealFact> mealFacts;
  private final List<Element> hiddenMealFacts;

  private MealMetaData(
      String name,
      Map<Class, MealFact> mealFacts,
      List<Element> hiddenMealFacts)
      throws MealException {
    if (checkTrimEmpty(name)) {
      throw new MealException("Name is empty or consists only of whitespace");
    } else {
      this.name = name.trim();
    }
    this.mealFacts = mealFacts;
    this.hiddenMealFacts = hiddenMealFacts;
  }

  public static MealMetaData createMealMetaData(
      String name,
      Map<Class, MealFact> mealFacts,
      List<Element> hiddenMealFacts) throws MealException {
    return new MealMetaData(name,
        mealFacts,
        hiddenMealFacts);
  }

  public static MealMetaData createEmptyMealMetaData() {
    return EMPTY_METADATA;
  }

  public String getName() {
    return name;
  }

  public Map<Class, MealFact> getMealFacts() {
    return mealFacts;
  }

  public List<Element> getHiddenMealFacts() {
    return hiddenMealFacts;
  }

  @Override
  public String toString() {
    return "MealMetaData{name=" + name + ", mealFacts=" + mealFacts + ", hiddenMealFacts=" + hiddenMealFacts + "}";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + name.hashCode();
    result = prime * result + mealFacts.hashCode();
    result = prime * result + hiddenMealFacts.hashCode();
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
    MealMetaData other = (MealMetaData) obj;
    return name.equals(other.name)
        && mealFacts.equals(other.mealFacts)
        && hiddenMealFacts.equals(other.hiddenMealFacts);
  }

  private boolean checkTrimEmpty(String str) {
    for (int i = 0; i < str.length(); i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
