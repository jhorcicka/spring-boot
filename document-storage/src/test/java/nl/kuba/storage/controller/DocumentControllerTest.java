package nl.kuba.storage.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import nl.kuba.storage.repository.DocumentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentControllerTest {
    private static final Long USER_ID = 100L;
    private static final String DOCUMENT_NAME = "name-placeholder";
    private static final String DOCUMENT_JSON =
            "{\"id\": \"\", \"created\": \"\", \"deleted\": null, \"userId\": " + USER_ID + ", \"name\": \"" + DOCUMENT_NAME
                    + "\", \"relativeFilePath\": null, \"type\": \"PASSPORT\", " + "\"notes\": \"notes for this document...\"}";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DocumentController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
    }

    @Test
    public void testAutowiredController() {
        assertNotNull(controller);
    }

    @Test
    public void basicCRUD() throws Exception {
        // get empty list
        final MvcResult getListResult1 = mvc.perform(get("/documents/user/" + USER_ID)).andExpect(status().isOk()).andReturn();
        assertEquals("[]", getListResult1.getResponse().getContentAsString());

        // get one document => NOT_FOUND
        mvc.perform(get("/documents/1/user/" + USER_ID)).andExpect(status().isNotFound());

        // put one document
        final MvcResult putResult = mvc
                .perform(put("/documents/user/" + USER_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(DOCUMENT_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        final Long documentId = Long.parseLong(putResult.getResponse().getContentAsString());

        // get non empty list
        final MvcResult getListResult2 = mvc.perform(get("/documents/user/" + USER_ID)).andExpect(status().isOk()).andReturn();
        final String foundDocumentListString = getListResult2.getResponse().getContentAsString();
        assertTrue(foundDocumentListString.startsWith("[{\"id\":"));

        // get one document
        final MvcResult getDocumentResult2 =
                mvc.perform(get("/documents/" + documentId + "/user/" + USER_ID)).andExpect(status().isOk()).andReturn();
        final String foundDocumentString = getDocumentResult2.getResponse().getContentAsString();
        assertTrue(foundDocumentString.startsWith("{\"id\":" + documentId));
        assertTrue(foundDocumentString.contains(DOCUMENT_NAME));

        // post document update
        final String newDocumentName = "new-document-name";
        mvc
                .perform(post("/documents/" + documentId + "/user/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(foundDocumentString.replace(DOCUMENT_NAME, newDocumentName)))
                .andExpect(status().isOk());

        // get updated document
        final MvcResult getDocumentResult3 =
                mvc.perform(get("/documents/" + documentId + "/user/" + USER_ID)).andExpect(status().isOk()).andReturn();
        assertTrue(getDocumentResult3.getResponse().getContentAsString().contains(newDocumentName));

        // delete the document
        mvc.perform(delete("/documents/" + documentId + "/user/" + USER_ID)).andExpect(status().isOk());

        // get empty list
        final MvcResult getListResult3 = mvc.perform(get("/documents/user/" + USER_ID)).andExpect(status().isOk()).andReturn();
        assertEquals("[]", getListResult3.getResponse().getContentAsString());
    }

    @Test
    public void uploadAndDownloadFile() throws Exception {
        // put one document
        final MvcResult putResult = mvc
                .perform(put("/documents/user/" + USER_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(DOCUMENT_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        final Long documentId = Long.parseLong(putResult.getResponse().getContentAsString());

        // upload a file
        final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("testDocument.pdf");
        final MockMultipartFile file = new MockMultipartFile("file", resourceAsStream);
        mvc
                .perform(multipart("/documents/" + documentId + "/user/" + USER_ID + "/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(Charset.defaultCharset()))
                .andExpect(status().isOk());

        // download the file
        mvc.perform(get("/documents/" + documentId + "/user/" + USER_ID + "/download")).andExpect(status().isOk());
    }

    @Test
    public void incorrectUserId() throws Exception {
        // put one document
        final MvcResult putResult = mvc
                .perform(put("/documents/user/" + USER_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(DOCUMENT_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        final Long documentId = Long.parseLong(putResult.getResponse().getContentAsString());

        // get the document
        mvc.perform(get("/documents/" + documentId + "/user/" + USER_ID)).andExpect(status().isOk());
        mvc.perform(get("/documents/" + documentId + "/user/" + (USER_ID + 1))).andExpect(status().isNotFound());
    }

    @Test
    public void canNotCreateMultipleDocumentsWithSameNameForOneUser() throws Exception {
        // put one document
        mvc
                .perform(put("/documents/user/" + USER_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(DOCUMENT_JSON))
                .andExpect(status().isCreated());
        // put the same document again
        mvc
                .perform(put("/documents/user/" + USER_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(DOCUMENT_JSON))
                .andExpect(status().isExpectationFailed());
    }

    @Test
    public void canCreateSameDocumentForDifferentUsers() throws Exception {
        // put one document
        mvc
                .perform(put("/documents/user/" + USER_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(DOCUMENT_JSON))
                .andExpect(status().isCreated());

        // put the same document again, but for a different user
        final Long anotherUserId = USER_ID + 1;
        mvc
                .perform(put("/documents/user/" + anotherUserId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(DOCUMENT_JSON.replace(USER_ID.toString(), anotherUserId.toString())))
                .andExpect(status().isCreated());
    }
}
