package nl.kuba.storage.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentControllerTest {
    private static final Long USER_ID = 100L;
    private static final String DOCUMENT_NAME = "name-placeholder";
    private static final String DOCUMENT_JSON =
            "{\"id\": \"\", \"created\": \"\", \"deleted\": null, \"userId\": " + USER_ID + ", \"name\": \"" + DOCUMENT_NAME
                    + "\", \"relativeFilePath\": null, \"type\": \"PASSPORT\", " + "\"notes\": \"notes for this document...\"}";

    /*
    TODO Test cases:
        * can't get any document if I don't provide correct user_id
        * getting correct HTTP status codes (if requests succeed and if they fail)
        * can't create a document with existing name for a given user
        * can create a document with the same name for different users
     */

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void autowiredController() {
        assertNotNull(controller);
    }

    @Test
    public void testCRUD() throws Exception {
        // get empty list
        final MvcResult getListResult1 = mvc.perform(get("/documents/user/" + USER_ID)).andExpect(status().isOk()).andReturn();
        assertEquals("[]", getListResult1.getResponse().getContentAsString());

        // get one document => NOT_FOUND
        mvc.perform(get("/documents/1/user/" + USER_ID)).andExpect(status().isNotFound());

        // put one document
        mvc
                .perform(put("/documents/user/" + USER_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(DOCUMENT_JSON))
                .andExpect(status().isCreated());

        // get non empty list
        final MvcResult getListResult2 = mvc.perform(get("/documents/user/" + USER_ID)).andExpect(status().isOk()).andReturn();
        final String foundDocumentListString = getListResult2.getResponse().getContentAsString();
        assertTrue(foundDocumentListString.startsWith("[{\"id\":"));
        final Long documentId = parseFirstIdFromListString(foundDocumentListString);

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

    /**
     * '[{"id":42, ... }]' => 42
     * @param json
     * @return
     */
    private static Long parseFirstIdFromListString(final String json) {
        final int firstColonIndex = json.indexOf(":");
        final int firstCommaIndex = json.indexOf(",");
        return Long.parseLong(json.substring(firstColonIndex + 1, firstCommaIndex));
    }

    @Test
    public void downloadFile() throws Exception {
        //mvc.perform(get("/documents/" + DOCUMENT_ID + "/user/" + USER_ID + "/download")).andExpect(status().isNotFound());
    }

    @Test
    public void uploadFile() throws Exception {
        /*
        mvc
                .perform(put("/documents/" + DOCUMENT_ID + "/user/" + USER_ID + "/upload")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(DOCUMENT_JSON))
                .andExpect(status().isOk());
         */
    }
}