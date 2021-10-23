package nl.kuba.storage.repository;

import nl.kuba.storage.model.Document;
import nl.kuba.storage.model.DocumentFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class DocumentRepositoryTest {
    @Autowired
    private DocumentRepository repository;

    private Document document;
    private List<Document> documentList;

    @Before
    public void setUp() throws Exception {
        documentList = DocumentFactory.createAllTypesOfDocumentsList();
        document = DocumentFactory.createDocument();
        documentList.add(document);

        repository.saveAll(documentList);
    }

    @Test
    public void findByName() {
        final List<Document> foundByName = repository.findByName(document.getName());
        assertEquals(1, foundByName.size());
        assertEquals(document.getName(), foundByName.get(0).getName());
    }

    @Test
    public void findByType() {
        final List<Document> foundByType = repository.findByType(document.getType());
        assertEquals(2, foundByType.size());
        assertEquals(document.getType(), foundByType.get(0).getType());
    }
}