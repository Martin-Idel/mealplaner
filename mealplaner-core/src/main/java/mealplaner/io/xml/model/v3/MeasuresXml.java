// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import static mealplaner.model.recipes.Measure.NONE;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.io.xml.util.MeasuresMapAdapter;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.Measures;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MeasuresXml {
  public final Measure primaryMeasure;
  @XmlJavaTypeAdapter(value = MeasuresMapAdapter.class)
  public final Map<Measure, NonnegativeFraction> secondaryMeasures;


  public MeasuresXml() {
    this(NONE, new HashMap<>());
  }

  public MeasuresXml(Measures measures) {
    this(measures.getPrimaryMeasure(), measures.getSecondaries());
  }

  public MeasuresXml(Measure primaryMeasure, Map<Measure, NonnegativeFraction> secondaryMeasures) {
    this.primaryMeasure = primaryMeasure;
    this.secondaryMeasures = secondaryMeasures;
  }

}
