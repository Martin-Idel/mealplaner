package mealplaner.xml.util;

import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import mealplaner.xml.model.v2.RecipeMap;

public class UuidMapAdapter extends XmlAdapter<RecipeMap, Map<UUID, Integer>> {

  @Override
  public RecipeMap marshal(Map<UUID, Integer> recipe) throws Exception {
    return new RecipeMap(recipe.entrySet().stream()
        .map(entry -> new RecipeMap.KeyValuePair(entry.getKey().toString(), entry.getValue()))
        .collect(Collectors.toList()));
  }

  @Override
  public Map<UUID, Integer> unmarshal(RecipeMap map) throws Exception {
    return map.recipeList.stream()
        .collect(toMap(entry -> fromString(entry.uuidKey), entry -> entry.value));
  }
}
