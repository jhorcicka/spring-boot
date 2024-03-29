package nl.kuba.storage.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentTest {

    @Test
    public void testFactory() {
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
    public void testBuilder() {
        final Document document = Document
                .builder()
                .id(DocumentFactory.ID)
                .created(new Date())
                .deleted(null)
                .userId(DocumentFactory.USER_ID)
                .name(DocumentFactory.NAME)
                .relativeFilePath(DocumentFactory.RELATIVE_PATH)
                .type(DocumentFactory.TYPE)
                .notes(DocumentFactory.NOTES)
                .build();
        assertEquals(DocumentFactory.ID, document.getId());
        assertNotNull(document.getCreated());
        assertNull(document.getDeleted());
        assertEquals(DocumentFactory.USER_ID, document.getUserId());
        assertEquals(DocumentFactory.NAME, document.getName());
        assertEquals(DocumentFactory.RELATIVE_PATH, document.getRelativeFilePath());
        assertEquals(DocumentFactory.TYPE, document.getType());
        assertEquals(DocumentFactory.NOTES, document.getNotes());
    }
}
