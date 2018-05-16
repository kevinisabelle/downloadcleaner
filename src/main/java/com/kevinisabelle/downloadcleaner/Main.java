/*
 * DesktopApplication1View.java
 */
package com.kevinisabelle.downloadcleaner;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * The application's main frame.
 */
public class Main {

  public static void main(String[] args) {

    try {

      System.out.println("Starting analysis...");

      UpdaterProperties.getInstance();

      List<DownloadedFile> foundFiles = AnalyzeFolders();

      //System.out.println("Total video files: " + foundFiles.size() + " Total Size: " + foundFiles.stream().mapToLong(i -> i.file.length()).sum() / 1024 + " MB");
      System.out.println("Needing fix: " + foundFiles.stream().filter(s -> s.needsFix()).count() + " Total Size: " + foundFiles.stream().filter(s -> s.needsFix()).mapToLong(i -> i.file.length()).sum() / 1024 + " MB");

      for (DownloadedFile df : foundFiles) {

        if (df.needsFix()) {
          System.out.println(df.newFilename() + " <-- ");
          System.out.println(df.file.getAbsolutePath());

          System.out.print("ok (o/n)? ");

          byte[] respArr = new byte[16];

          int read = System.in.read(respArr);
          char c = (char) respArr[0];

          if (c == 'o' || c == 'O') {

            System.out.println("Moving...");

            try {

              FileUtils.moveFile(
                      FileUtils.getFile(df.file.getAbsolutePath()),
                      FileUtils.getFile(df.newFilename()));

            } catch (Exception e) {
              System.out.println("Error moving file: " + e.getMessage());
            }

          } else {

            System.out.println("skipping...");

          }
        }

      }

      System.out.println("Completed.");
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public static List<DownloadedFile> AnalyzeFolders() {

    List<DownloadedFile> foundFiles = new LinkedList<DownloadedFile>();

    File file = new File(UpdaterProperties.getInstance().getLocation());

    AddSubfolders(file, foundFiles);

    return foundFiles;

  }

  public static void AddSubfolders(File file, List<DownloadedFile> filesList) {

    for (File fileF : file.listFiles()) {

      if (fileF.isDirectory()) {

        if ("Series".equals(fileF.getName()) || "Films".equals(fileF.getName())) {
          continue;
        }

        AddSubfolders(fileF, filesList);

      } else {

        AddFile(fileF, filesList);

      }
    }
  }

  public static void AddFile(File file, List<DownloadedFile> filesList) {

    String[] types = UpdaterProperties.getInstance().getFiletypes().split(",");

    boolean ok = false;

    for (String type : types) {

      if (file.getName().endsWith(type)) {

        ok = true;
        break;
      }
    }

    if (!ok) {
      return;
    }

    try  {    
    DownloadedFile dfile = new DownloadedFile(file);
    filesList.add(dfile);
    } catch (Exception e){
      e.printStackTrace();
    }

  }
}
