// SPDX-License-Identifier: MIT

package mealplaner.plugins.comment.mealextension;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentFact implements MealFact, MealFactXml {
  private final String comment;

  public CommentFact() {
    this.comment = "";
  }

  public CommentFact(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentFact that = (CommentFact) o;
    return comment.equals(that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comment);
  }

  @Override
  public String toString() {
    return "[comment='" + comment + "']";
  }
}
