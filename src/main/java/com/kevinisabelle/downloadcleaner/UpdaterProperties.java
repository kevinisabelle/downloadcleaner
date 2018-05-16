/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.downloadcleaner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 *
 * @author kisabelle
 */
public class UpdaterProperties {

  private static final String propertiesFileRelativeLocation = "../conf/config.properties";
  private static UpdaterProperties instance;
  private Properties props = new Properties();

  public static final String CRAP_PATTERNS = "CRAP_PATTERNS";
  public static final String LANGUAGES_PATTERNS = "LANGUAGES_PATTERNS";
  public static final String SERIES_PATTERNS = "SERIES_PATTERNS";
  public static final String LOCATION = "LOCATION";
  public static final String FILETYPES= "FILETYPES";

  public static UpdaterProperties getInstance() {
    if (instance == null) {
      instance = new UpdaterProperties();
      //Load properties
      instance.loadProperties();
    }

    return instance;
  }

  public String getLocation() {
    if (props.get(LOCATION) == null) {
      return "";
    }
    return props.get(LOCATION).toString();
  }
  
  public String getFiletypes() {
    if (props.get(FILETYPES) == null) {
      return "";
    }
    return props.get(FILETYPES).toString();
  }

  public String getCrapPatterns() {
    if (props.get(CRAP_PATTERNS) == null) {
      return "";
    }
    return props.get(CRAP_PATTERNS).toString();
  }

  public String getLanguagePatterns() {
    if (props.get(LANGUAGES_PATTERNS) == null) {
      return "";
    }
    return props.get(LANGUAGES_PATTERNS).toString();
  }

  public String getSeriesPatterns() {
    if (props.get(SERIES_PATTERNS) == null) {
      return "";
    }
    return props.get(SERIES_PATTERNS).toString();
  }

  private void loadProperties() {
    try {
      File file = new File(propertiesFileRelativeLocation);
      FileInputStream fis = new FileInputStream(file);
      props.load(fis);
    } catch (Exception e) {
      //e.printStackTrace();
      System.out.println("Could not load properties: " + e.getMessage());

    }
  }

  public void saveProperties() {
    try {
      File file = new File(propertiesFileRelativeLocation);
      FileOutputStream fos = new FileOutputStream(file);
      props.store(fos, "Mp3 Cover Downloader Config");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setProperty(String key, String value) {
    props.setProperty(key, value);
  }

  public String getProperty(String key) {
    return props.getProperty(key);
  }
}
