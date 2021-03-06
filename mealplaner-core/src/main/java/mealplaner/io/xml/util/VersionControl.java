// SPDX-License-Identifier: MIT

package mealplaner.io.xml.util;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class VersionControl {
  private static final Logger logger = LoggerFactory.getLogger(VersionControl.class);

  private VersionControl() {
  }

  public static int getVersion(String filePath) {
    try {
      File inputFile = new File(filePath);
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      docFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
      Document parsedDocument = documentBuilder.parse(inputFile);
      parsedDocument.getDocumentElement().normalize();
      return getVersionNumberFromTopLevelVersionNode(parsedDocument);
    } catch (ParserConfigurationException | SAXException ex) {
      logger.warn("Exception on parsing XML with relative path {}.", filePath);
    } catch (IOException ex) {
      logger.warn("Exception on parsing XML with relative path {}. Maybe the file is missing?", filePath);
    }
    return 0;
  }

  private static int getVersionNumberFromTopLevelVersionNode(Document parsedDocument) {
    Node modelNode = parsedDocument.getFirstChild();
    if (modelNode != null) {
      NodeList allChildren = modelNode.getChildNodes();
      for (int i = 0; i < allChildren.getLength(); i++) {
        Node potentialVersionNode = allChildren.item(i);
        if (potentialVersionNode.getNodeType() == Node.ELEMENT_NODE
            && potentialVersionNode.getNodeName().equals("version")) {
          try {
            return Integer.parseInt(potentialVersionNode.getTextContent());
          } catch (NumberFormatException ex) {
            logger.warn("There is a top-level node with name \"version\" "
                + " which does not contain a version number but instead contains: {}",
                potentialVersionNode.getTextContent());
          }
        }
      }
    }
    logger.warn("Parser could not find a toplevel XML element named \"version\"."
        + " Are you sure you have a valid save file?");
    return 0;
  }
}
