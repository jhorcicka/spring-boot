package nl.kuba.storage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;

@Component
public class FileStorageService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

  private static final int NAME_LENGTH = 16;
  private static final int BUFFER_SIZE = 1024;

  @Value("${nl.kuba.storage.files.directory}")
  private String directory;

  public String store(final InputStream inputStream) throws IOException {
    final String newName = generateRandomFileName();
    final File rootDirectory = new File(directory);
    if (!rootDirectory.exists()) {
      rootDirectory.mkdirs();
    }
    final String filePath = directory + File.separator + newName;
    LOGGER.debug("store(): filePath={}", filePath);
    try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
      byte[] buffer = new byte[BUFFER_SIZE];
      while (inputStream.read(buffer) > 0) {
        fileOutputStream.write(buffer);
      }
    } finally {
      inputStream.close();
    }

    return newName;
  }

  public File get(final String relativePath) throws IOException {
    final String filePath = directory + File.separator + relativePath;
    LOGGER.debug("get(): filePath={}", filePath);
    final File file = new File(filePath);
    if (!file.exists()) {
        throw new FileNotFoundException("File was not found in the storage directory. ");
    }
    return file;
  }

  private static String generateRandomFileName() {
    /*
    Assumption:
    Unix-based operating system => no need for file extensions (.pdf, .jpg, ...).
     */
    final StringBuilder name = new StringBuilder();
    for (int i = 0; i < NAME_LENGTH; i++) {
      final char randomLowerCaseLetter = (char)('a' + new Random().nextInt('z' - 'a'));
      name.append(randomLowerCaseLetter);
    }
    return name.toString();
  }
}
