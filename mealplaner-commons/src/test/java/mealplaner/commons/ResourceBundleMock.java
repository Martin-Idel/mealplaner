// SPDX-License-Identifier: MIT

package mealplaner.commons;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ResourceBundleMock extends ResourceBundle {
  private final transient Map<String, String> tm = new TreeMap<>();

  public ResourceBundleMock(Map<String, String> properties) {
    tm.putAll(properties);
  }

  @Override
  protected void setParent(ResourceBundle parent) {
    // we don't want to get into an infinite loop
  }

  @Override
  protected Object handleGetObject(String key) {
    return tm.get(key);
  }

  @Override
  public Enumeration<String> getKeys() {
    return Collections.enumeration(tm.keySet());
  }
}
