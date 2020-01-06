// SPDX-License-Identifier: MIT

package mealplaner.io.xml.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import mealplaner.commons.NonnegativeFraction;

public class NonnegativeFractionAdapter extends XmlAdapter<String, NonnegativeFraction> {

  @Override
  public NonnegativeFraction unmarshal(String v) {
    return NonnegativeFraction.parse(v);
  }

  @Override
  public String marshal(NonnegativeFraction v) {
    return v.toString();
  }
}
