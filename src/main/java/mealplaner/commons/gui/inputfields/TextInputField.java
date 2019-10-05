// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import java.util.function.Function;

public class TextInputField extends TextFactInputField<String> {
  public TextInputField(String label, String name, int orderNumber) {
    super(label, name, orderNumber, Function.identity());
  }
}
