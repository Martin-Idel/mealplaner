// SPDX-License-Identifier: MIT

package mealplaner.commons;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ResourceBundleMock extends ResourceBundle {
  private final TreeMap<String, String> tm = new TreeMap<>();

  public ResourceBundleMock(TreeMap<String, String> properties) {
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
