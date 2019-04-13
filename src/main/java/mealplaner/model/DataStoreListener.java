// SPDX-License-Identifier: MIT

package mealplaner.model;

public interface DataStoreListener {
  void updateData(DataStoreEventType event);
}
