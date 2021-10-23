package nl.kuba.storage.controller;

import nl.kuba.storage.model.Document;
import nl.kuba.storage.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// todo:
// security
// file upload
// errors when things do not work...

@Controller
public class DocumentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentRepository repository;

    @GetMapping(path = "/")
    public @ResponseBody Iterable<Document> getAllDocuments() {
        LOGGER.debug("getAllDocuments");
        return repository.findAll();
    }

    @GetMapping(path = "/user/{user_id}")
    public @ResponseBody Iterable<Document> getUserDocuments() {
        LOGGER.debug("getUserDocuments");
        return repository.findAll();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody Document getDocument(@PathVariable final Long id) {
        LOGGER.debug("getDocument: id={}", id);
        return repository.findById(id).orElse(null);
    }

    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveDocument(@RequestBody final Document document) {
        LOGGER.debug("saveDocument: document={}", document);
        repository.save(Document.builder()
                .created(new Date())
                .userId(document.getUserId())
                .name(document.getName())
                .type(document.getType())
                .notes(document.getNotes())
                .build());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveAllDocuments(@RequestBody final List<Document> documentList) {
        LOGGER.debug("saveAllDocuments: size={}", documentList.size());
        repository.saveAll(documentList);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDocument(@PathVariable final Long id, @RequestBody final Document document) {
        LOGGER.debug("updateDocument: id={}", id);
        repository.updateDocumentById(id, document.getName(), document.getNotes());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable final Long id) {
        LOGGER.debug("deleteDocument: id={}", id);
        repository.findById(id).ifPresent(document -> repository.delete(document));
        return ResponseEntity.ok().build();
    }
}
