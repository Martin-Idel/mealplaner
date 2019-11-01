package mealplaner.commons;

import static java.util.Collections.enumeration;
import static java.util.Collections.list;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MultiResourceBundle extends ResourceBundle {
  private List<ResourceBundle> resourceBundles = new ArrayList<>();

  public void addResourceBundle(ResourceBundle resourceBundle) {
    resourceBundles.add(resourceBundle);
  }

  @Override
  public boolean containsKey(String key) {
    return resourceBundles.stream().anyMatch(bundle -> bundle.containsKey(key));
  }

  @Override
  protected Object handleGetObject(String key) {
    var bundle = resourceBundles.stream().filter(resourceBundle -> resourceBundle.containsKey(key))
        .findFirst();
    return bundle.map(value -> value.getObject(key)).orElse(null);
  }

  @Override
  public Locale getLocale() {
    return resourceBundles.get(0).getLocale();
  }

  @Override
  public Enumeration<String> getKeys() {
    List<String> keys = resourceBundles.stream()
        .flatMap(resourceBundle -> list(resourceBundle.getKeys()).stream())
        .collect(Collectors.toList());

    return enumeration(keys);
  }
}
