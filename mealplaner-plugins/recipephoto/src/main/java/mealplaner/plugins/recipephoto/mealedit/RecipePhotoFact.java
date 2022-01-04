// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto.mealedit;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipePhotoFact implements MealFact, MealFactXml {
  @XmlJavaTypeAdapter(NioPathAdapter.class)
  private Path path = null;

  public RecipePhotoFact() {
  }

  public RecipePhotoFact(Path path) {
    this.path = path;
  }

  public Optional<Path> getPath() {
    return path == null ? Optional.empty() : Optional.of(path);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecipePhotoFact that = (RecipePhotoFact) o;
    return path.equals(that.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path);
  }

  @Override
  public String toString() {
    return "[path='" + path + "']";
  }
}
