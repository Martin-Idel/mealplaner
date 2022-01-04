// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto.mealedit;

import java.nio.file.Path;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class NioPathAdapter extends XmlAdapter<String, Object> {
  @Override
  public Object unmarshal(String v) throws Exception {
    return Path.of(v);
  }

  @Override
  public String marshal(Object v) {
    if (!(v instanceof Path)) {
      throw new IllegalArgumentException("Object not instance of path");
    }
    return v.toString();
  }
}
