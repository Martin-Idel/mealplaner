// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import java.util.function.Function;

public class CheckboxInputField extends CheckboxFactInputField<Boolean> {
  public CheckboxInputField(String label, String name, int orderNumber) {
    super(label, name, orderNumber, Function.identity());
  }
}
