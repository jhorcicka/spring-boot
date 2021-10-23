package nl.kuba.storage.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testFullBuilder() {
        final Document document = DocumentFactory.createDocument();
        assertEquals(DocumentFactory.ID, document.getId());
        assertEquals(DocumentFactory.USER_ID, document.getUserId());
        assertEquals(DocumentFactory.NAME, document.getName());
        assertEquals(DocumentFactory.RELATIVE_PATH, document.getRelativeFilePath());
        assertEquals(DocumentFactory.TYPE, document.getType());
        assertEquals(DocumentFactory.NOTES, document.getNotes());
        assertNotNull(document.getCreated());
        assertNull(document.getDeleted());
    }

    @Test
    public void testBuilderWithDefaultValues() {
        final Document document = Document
                .builder()
                .userId(DocumentFactory.USER_ID)
                .name(DocumentFactory.NAME)
                .relativeFilePath(DocumentFactory.RELATIVE_PATH)
                .type(DocumentFactory.TYPE)
                .notes(DocumentFactory.NOTES)
                .build();
        assertEquals(new Long(0), document.getId());
        assertEquals(DocumentFactory.USER_ID, document.getUserId());
        assertEquals(DocumentFactory.NAME, document.getName());
        assertEquals(DocumentFactory.RELATIVE_PATH, document.getRelativeFilePath());
        assertEquals(DocumentFactory.TYPE, document.getType());
        assertEquals(DocumentFactory.NOTES, document.getNotes());
        assertNotNull(document.getCreated());
        assertNull(document.getDeleted());
    }
}
