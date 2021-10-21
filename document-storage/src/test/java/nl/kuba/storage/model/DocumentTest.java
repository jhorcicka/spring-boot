package nl.kuba.storage.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.Test;

class DocumentTest {
    private static final Long ID = 1L;
    private static final Long USER_ID = 100L;
    private static final String NAME = "document-name-1";
    private static final String RELATIVE_PATH = "document.pdf";
    private static final Document.Type TYPE = Document.Type.PASSPORT;
    private static final String NOTES = "testing document notes...";

    @Test
    void testFullBuilder() {
        final Document document = Document
                .builder()
                .id(ID)
                .created(new Date())
                .userId(USER_ID)
                .name(NAME)
                .relativeFilePath(RELATIVE_PATH)
                .type(TYPE)
                .notes(NOTES)
                .build();
        assertEquals(ID, document.getId());
        assertEquals(USER_ID, document.getUserId());
        assertEquals(NAME, document.getName());
        assertEquals(RELATIVE_PATH, document.getRelativeFilePath());
        assertEquals(TYPE, document.getType());
        assertEquals(NOTES, document.getNotes());
        assertNotNull(document.getCreated());
        assertNull(document.getDeleted());
    }

    @Test
    void testBuilderWithDefaultValues() {
        final Document document = Document
                .builder()
                .userId(USER_ID)
                .name(NAME)
                .relativeFilePath(RELATIVE_PATH)
                .type(TYPE)
                .notes(NOTES)
                .build();
        assertEquals(0L, document.getId());
        assertEquals(USER_ID, document.getUserId());
        assertEquals(NAME, document.getName());
        assertEquals(RELATIVE_PATH, document.getRelativeFilePath());
        assertEquals(TYPE, document.getType());
        assertEquals(NOTES, document.getNotes());
        assertNotNull(document.getCreated());
        assertNull(document.getDeleted());
    }

    @Test
    void testDeletion() {
        final Document document = Document
                .builder()
                .id(ID)
                .userId(USER_ID)
                .build();
        assertEquals(ID, document.getId());
        assertNull(document.getDeleted());
        document.delete();
        assertNotNull(document.getDeleted());
    }
}