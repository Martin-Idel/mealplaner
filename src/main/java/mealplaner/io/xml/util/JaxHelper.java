package mealplaner.io.xml.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import mealplaner.commons.errorhandling.MealException;

public final class JaxHelper {
  private JaxHelper() {
  }

  @SuppressWarnings("unchecked")
  public static <T> T read(String savePath, Class<T> type) {
    try {
      JAXBContext jc = JAXBContext.newInstance(type);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      File xml = new File(savePath);
      return (T) unmarshaller.unmarshal(xml);
    } catch (JAXBException e) {
      throw new MealException("File " + savePath + " could not be parsed.", e);
    }
  }

  public static <T> void save(Class<T> type, T data, String filePath) {
    try (OutputStream outputStream = new FileOutputStream(filePath)) {
      JAXBContext jc = JAXBContext.newInstance(type);
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(data, outputStream);
    } catch (JAXBException e) {
      throw new MealException("File " + filePath + " could not be parsed: " + e.getMessage(), e);
    } catch (FileNotFoundException e) {
      throw new MealException("File " + filePath + " was not be found: " + e.getMessage(), e);
    } catch (IOException e) {
      throw new MealException("File " + filePath + " could not be loaded: " + e.getMessage(), e);
    }
  }
}
