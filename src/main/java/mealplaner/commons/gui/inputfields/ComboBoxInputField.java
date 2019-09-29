// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import java.util.function.Function;

public class ComboBoxInputField<E extends Enum<E>> extends ComboBoxFactInputField<E, E> {

  public ComboBoxInputField(String label, String name, Class<E> enumType, E defaultValue, int orderNumber) {
    super(label, name, enumType, defaultValue, orderNumber, Function.identity());
  }
}
