// SPDX-License-Identifier: MIT

package mealplaner.io.xml.util;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.io.xml.model.v3.MeasuresMap;
import mealplaner.model.recipes.Measure;

public class MeasuresMapAdapter extends XmlAdapter<MeasuresMap, Map<Measure, NonnegativeFraction>> {

  @Override
  public MeasuresMap marshal(Map<Measure, NonnegativeFraction> recipe) {
    return new MeasuresMap(recipe.entrySet().stream()
        .map(entry -> new MeasuresMap.KeyValuePair(entry.getKey().toString(),
            entry.getValue().toString()))
        .collect(Collectors.toList()));
  }

  @Override
  public Map<Measure, NonnegativeFraction> unmarshal(MeasuresMap map) {
    return map.recipeList.stream()
        .collect(toMap(entry -> Measure.valueOf(entry.measureKey),
            entry -> NonnegativeFraction.parse(entry.value)));
  }
}
