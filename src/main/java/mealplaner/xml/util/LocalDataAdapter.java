package mealplaner.xml.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDataAdapter extends XmlAdapter<String, LocalDate> {
  @Override
  public LocalDate unmarshal(String v) throws DateTimeParseException {
    return LocalDate.parse(v);
  }

  @Override
  public String marshal(LocalDate v) {
    return v.toString();
  }
}
