package mealplaner.xml.util;

import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class UuidMapAdapter extends XmlAdapter<Map<String, Integer>, Map<UUID, Integer>> {

  @Override
  public Map<String, Integer> marshal(Map<UUID, Integer> recipe) throws Exception {
    return recipe.entrySet().stream()
        .collect(toMap(entry -> entry.getKey().toString(), Entry::getValue));
  }

  @Override
  public Map<UUID, Integer> unmarshal(Map<String, Integer> recipe) throws Exception {
    return recipe.entrySet().stream()
        .collect(toMap(entry -> fromString(entry.getKey().toString()), Entry::getValue));
  }
}
