package nl.kuba.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;

@Component
public class FileStorageService {
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

  private static String generateRandomFileName() {
    final StringBuilder name = new StringBuilder();
    for (int i = 0; i < NAME_LENGTH; i++) {
      final char randomLowerCaseLetter = (char)('a' + new Random().nextInt('z' - 'a'));
      name.append(randomLowerCaseLetter);
    }
    return name.toString();
  }
}
