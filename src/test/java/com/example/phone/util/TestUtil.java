package com.example.phone.util;

import static org.springframework.util.ResourceUtils.getFile;

import java.io.File;
import java.nio.file.Files;

public class TestUtil {

  public static String getFileContent(final String fileName) throws Exception {
    File file = getFile(fileName);
    String fileRead = new String(Files.readAllBytes(file.toPath()));
    return fileRead;
  }
}
