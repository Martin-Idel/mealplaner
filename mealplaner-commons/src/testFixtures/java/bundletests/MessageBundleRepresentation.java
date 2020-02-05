// SPDX-License-Identifier: MIT

package bundletests;

import java.util.List;

import org.assertj.core.presentation.StandardRepresentation;

public class MessageBundleRepresentation extends StandardRepresentation {
  @Override
  protected String fallbackToStringOf(Object object) {
    if (object instanceof List) {
      StringBuilder builder = new StringBuilder("Size: ");
      builder.append(((List<?>) object).size());
      ((List<?>) object).forEach(builder::append);
      return builder.toString();
    }
    return super.fallbackToStringOf(object);
  }
}
