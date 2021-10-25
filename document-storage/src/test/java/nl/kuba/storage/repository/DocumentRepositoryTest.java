package nl.kuba.storage.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.kuba.storage.model.Document;
import nl.kuba.storage.model.DocumentFactory;

@RunWith(SpringRunner.class)
@DataJpaTest
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
    public void findByUserIdAndName() {
        final List<Document> foundByName = repository.findByUserIdAndName(document.getUserId(), document.getName());
        assertEquals(1, foundByName.size());
        assertEquals(document.getName(), foundByName.get(0).getName());
    }
}