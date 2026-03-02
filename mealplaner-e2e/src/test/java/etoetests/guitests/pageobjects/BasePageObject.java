// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;

import org.assertj.core.api.Assertions;

import etoetests.guitests.helpers.SwingTestHelper;

public class BasePageObject {
  protected final JFrame frame;
  protected final SwingTestHelper helper;

  protected BasePageObject(JFrame frame) {
    this.frame = frame;
    this.helper = new SwingTestHelper(frame);
  }

  protected void assertTableContents(JTable table, String[][] expectedContent) {
    for (int row = 0; row < expectedContent.length; row++) {
      for (int col = 0; col < expectedContent[row].length; col++) {
        Object actualValue = table.getValueAt(row, col);
        String expectedString = expectedContent[row][col];
        String actualString = actualValue != null ? actualValue.toString() : null;
        Assertions.assertThat(actualString).isEqualTo(expectedString);
      }
    }
  }

  protected JTable findTableInDialog(JDialog dialog) {
    return findNamedTableInDialogOrFallback(dialog, null);
  }

  protected JTable findNamedTableInDialogOrFallback(JDialog dialog, String tableName) {
    try {
      if (tableName != null) {
        JTable namedTable = helper.findComponentByNameOrNull(dialog, tableName, JTable.class);
        if (namedTable != null) {
          return namedTable;
        }
      }
      return helper.findFirstComponentOfClass(dialog, JTable.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to find table in dialog", e);
    }
  }

  protected JTable findTableByName(JFrame frame, String tableName) {
    try {
      return helper.findComponentByName(frame, tableName);
    } catch (Exception e) {
      throw new RuntimeException("Failed to find table: " + tableName, e);
    }
  }
}