package mealplaner.commons.gui.editing;

import static java.lang.Integer.parseInt;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class NonnegativeIntegerTextFilter extends DocumentFilter {

  @Override
  public void insertString(FilterBypass filterBypass, int offset, String string,
      AttributeSet attributes)
      throws BadLocationException {
    Document doc = filterBypass.getDocument();
    StringBuilder composedString = new StringBuilder()
        .append(doc.getText(0, doc.getLength()))
        .insert(offset, string);

    if (isNonnegativeInteger(composedString.toString())) {
      super.insertString(filterBypass, offset, string, attributes);
    }
  }

  @Override
  public void remove(FilterBypass filterBypass, int offset, int length)
      throws BadLocationException {
    Document doc = filterBypass.getDocument();
    StringBuilder removedPartialString = new StringBuilder()
        .append(doc.getText(0, doc.getLength()))
        .delete(offset, offset + length);

    if (isNonnegativeInteger(removedPartialString.toString())) {
      super.remove(filterBypass, offset, length);
    }
  }

  @Override
  public void replace(FilterBypass filterBypass, int offset, int length, String string,
      AttributeSet attributes)
      throws BadLocationException {
    Document doc = filterBypass.getDocument();
    StringBuilder replacedString = new StringBuilder()
        .append(doc.getText(0, doc.getLength()))
        .replace(offset, offset + length, string);

    if (isNonnegativeInteger(replacedString.toString())) {
      super.replace(filterBypass, offset, length, string, attributes);
    }
  }

  private boolean isNonnegativeInteger(String text) {
    try {
      return "".equals(text) || parseInt(text) >= 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}