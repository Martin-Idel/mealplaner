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
  public final List<KeyValuePair> recipeList;

  public MeasuresMap() {
    this(new ArrayList<>());
  }

  public MeasuresMap(List<KeyValuePair> recipeList) {
    this.recipeList = recipeList;
  }

  public static class KeyValuePair {
    public final String measureKey;
    public final String value;

    public KeyValuePair() {
      this(Measure.NONE.toString(), "0");
    }

    public KeyValuePair(String key, String value) {
      this.measureKey = key;
      this.value = value;
    }
  }
}
