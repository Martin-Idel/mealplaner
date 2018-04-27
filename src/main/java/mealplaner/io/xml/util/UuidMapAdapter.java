// SPDX-License-Identifier: MIT

package mealplaner.io.xml.util;

import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.io.xml.model.v2.RecipeMap;

public class UuidMapAdapter extends XmlAdapter<RecipeMap, Map<UUID, NonnegativeFraction>> {

  @Override
  public RecipeMap marshal(Map<UUID, NonnegativeFraction> recipe) throws Exception {
    return new RecipeMap(recipe.entrySet().stream()
        .map(entry -> new RecipeMap.KeyValuePair(entry.getKey().toString(),
            entry.getValue().toString()))
        .collect(Collectors.toList()));
  }

  @Override
  public Map<UUID, NonnegativeFraction> unmarshal(RecipeMap map) throws Exception {
    return map.recipeList.stream()
        .collect(toMap(entry -> fromString(entry.uuidKey),
            entry -> NonnegativeFraction.parse(entry.value)));
  }
}
