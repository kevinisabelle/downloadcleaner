/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.downloadcleaner;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
public class DownloadedFile {

  public DownloadedFile(File file) {
    this.file = file;
    parseFilename();
  }

  public File file;
  public boolean IsMovie;

  public String newFilename;

  public String title;
  public String language;
  public String season;
  public String year;
  public String episode;

  public boolean needsFix = false;

  @Override
  public String toString() {

    StringBuilder result = new StringBuilder();

    if ( needsFix()){
      result.append("***");
    }
    
    result.append(title).append(" - ");

    if (IsMovie) {
      result.append(year).append(" - ");
    } else {
      result.append("S").append(season).append(" E").append(episode).append(" - ");
    }

    result.append(language);

    return result.toString();

  }

  private void parseFilename() {

    // Clean crap from file
    newFilename = file.getName();

    String[] crapPatterns = UpdaterProperties.getInstance().getCrapPatterns().split(",");

    for (String crap : crapPatterns) {
      newFilename = newFilename.replaceAll(crap, "").trim();
    }

    IsMovie = false;

    // Extract year or season
    if (newFilename.matches(UpdaterProperties.getInstance().getSeriesPatterns())) {

      Pattern p = Pattern.compile(UpdaterProperties.getInstance().getSeriesPatterns());
      Matcher m = p.matcher(newFilename);
      if (m.matches()) {
        int index = m.end();
        season = m.group(2);
        //System.out.println(season);

        episode = season.toUpperCase().split("E")[1];
        season = season.toUpperCase().split("E")[0].substring(1);
      }

    } else {

      IsMovie = true;

      Pattern p = Pattern.compile("(.*)\\.(20[0-9][0-9]|19[0-9][0-9])\\..*");
      Matcher m = p.matcher(newFilename);

      while (m.find()) {
        year = m.group(m.groupCount());
      }

    }

    title = newFilename.substring(0, newFilename.lastIndexOf("." + (IsMovie ? year : "S" + season + "E" + episode)));
    language = "";

    for (String language2 : UpdaterProperties.getInstance().getLanguagePatterns().split(",")) {

      if (newFilename.contains(language2)) {
        language = language2;
      }
    }
    
  }
  
  public boolean needsFix() {
    
    return !newFilename().equals(file.getAbsolutePath());
    
  }
  
  public String newFilename() {
    
    return getFolder() + title + "." + (IsMovie ? year : ("S" + season + "E" + episode))  + (language.equals("") ? "" : "." + language) + file.getName().substring(file.getName().lastIndexOf("."));
  }
  
  public String getFolder() {
    
    String result = UpdaterProperties.getInstance().getLocation();
    
    if (IsMovie){
      result += "\\Films\\";
    } else {
      result += "\\Series\\" + title + " - S" + season + (language.equals("") ? "" : " - " + language) + "\\";
    }
    
    return result;
    
  }

  // Extract
}
