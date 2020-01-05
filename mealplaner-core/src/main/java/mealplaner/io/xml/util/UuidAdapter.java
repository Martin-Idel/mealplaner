// SPDX-License-Identifier: MIT

package mealplaner.io.xml.util;

import java.util.UUID;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class UuidAdapter extends XmlAdapter<String, UUID> {

  @Override
  public UUID unmarshal(String v) {
    return UUID.fromString(v);
  }

  @Override
  public String marshal(UUID v) {
    return v.toString();
  }
}
