// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeMap {
  public final List<KeyValuePair> recipeList;

  public RecipeMap() {
    this(new ArrayList<>());
  }

  public RecipeMap(List<KeyValuePair> recipeList) {
    this.recipeList = recipeList;
  }

  public static class KeyValuePair {
    public final String uuidKey;
    public final String value;

    public KeyValuePair() {
      this(UUID.randomUUID().toString(), "0");
    }

    public KeyValuePair(String key, String value) {
      this.uuidKey = key;
      this.value = value;
    }
  }
}
