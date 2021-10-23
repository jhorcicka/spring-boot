package nl.kuba.storage.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DocumentFactory {
    public static final Long ID = 1L;
    public static final Long USER_ID = 100L;
    public static final String NAME = "document-name-1";
    public static final String RELATIVE_PATH = "document.pdf";
    public static final Document.Type TYPE = Document.Type.PASSPORT;
    public static final String NOTES = "testing document notes...";
    public static final int RANDOM_STRING_LENGTH = 10;

    public static Document createDocument() {
        return Document
                .builder()
                .id(ID)
                .created(new Date())
                .userId(USER_ID)
                .name(NAME)
                .relativeFilePath(RELATIVE_PATH)
                .type(TYPE)
                .notes(NOTES)
                .build();
    }

    public static List<Document> createAllTypesOfDocumentsList() {
        final List<Document> documentList = new ArrayList<>();

        Arrays.stream(Document.Type.values()).forEach(type -> {
            documentList.add(Document
                    .builder()
                    .id(getRandomLong())
                    .created(new Date())
                    .userId(getRandomLong())
                    .name(getRandomString(RANDOM_STRING_LENGTH))
                    .relativeFilePath(getRandomString(2 * RANDOM_STRING_LENGTH))
                    .type(type)
                    .notes(getRandomString(10 * RANDOM_STRING_LENGTH))
                    .build());
        });
        return documentList;
    }

    private static String getRandomString(final int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static Long getRandomLong() {
        return new Random().nextLong();
    }
}
