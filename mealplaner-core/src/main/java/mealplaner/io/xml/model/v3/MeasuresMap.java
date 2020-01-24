// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.model.recipes.Measure;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MeasuresMap {
  public final List<KeyValuePair> secondaryMeasures;

  public MeasuresMap() {
    this(new ArrayList<>());
  }

  public MeasuresMap(List<KeyValuePair> recipeList) {
    this.secondaryMeasures = recipeList;
  }

  public static class KeyValuePair {
    public final Measure measureKey;
    public final String value;

    public KeyValuePair() {
      this(Measure.NONE, "0");
    }

    public KeyValuePair(Measure key, String value) {
      this.measureKey = key;
      this.value = value;
    }
  }
}
