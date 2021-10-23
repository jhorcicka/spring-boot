package nl.kuba.storage.controller;

import nl.kuba.storage.model.Document;
import nl.kuba.storage.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

// todo:
// file upload

@Controller
@RequestMapping(value = "/documents")
public class DocumentController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

  @Autowired
  private DocumentRepository repository;

  @GetMapping(path = "/user/{userId}")
  public @ResponseBody
  Iterable<Document> getUserDocumentList(@PathVariable final Long userId) {
    LOGGER.debug("getUserDocumentList: userId={}", userId);
    return repository.findByUserId(userId);
  }

  @GetMapping(path = "{documentId}/user/{userId}")
  public @ResponseBody
  ResponseEntity<Document> getUserDocument(@PathVariable final Long documentId, @PathVariable final Long userId) {
    LOGGER.debug("getUserDocument: userId={}, documentId={}", userId, documentId);
    final List<Document> foundDocuments = repository.findByUserIdAndId(userId, documentId);
    if (foundDocuments.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } else {
      return ResponseEntity.ok().body(foundDocuments.get(0)); // documentId is unique
    }
  }

  @PutMapping(path = "/user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> saveDocument(@PathVariable final Long userId, @RequestBody final Document document) throws URISyntaxException {
    LOGGER.debug("saveDocument: userId={}, documentName={}", userId, document.getName());
    final List<Document> userDocumentsWithSameName =
        repository.findByUserIdAndName(document.getUserId(), document.getName());
    if (!userDocumentsWithSameName.isEmpty()) {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Provided document name is not unique. ");
    }
    final Document newDocument = repository.save(Document.builder()
        .created(new Date())
        .userId(document.getUserId())
        .name(document.getName())
        .type(document.getType())
        .notes(document.getNotes())
        .build());
    final URI uri = new URI("/documents/" + newDocument.getId() + "/user/" + document.getUserId());
    return ResponseEntity.created(uri).build();
  }

  @PostMapping(path = "{documentId}/user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateDocument(@PathVariable final Long documentId, @PathVariable final Long userId, @RequestBody final Document document) {
    LOGGER.debug("updateDocument: documentId={}, userId={}", documentId, userId);
    repository.updateUserDocument(userId, document.getId(), document.getName(), document.getNotes());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "/{id}/user/{userId}")
  public ResponseEntity<String> deleteDocument(@PathVariable final Long documentId, @PathVariable final Long userId) {
    LOGGER.debug("deleteDocument: documentId={}, userId={}", documentId, userId);
    final List<Document> foundDocuments = repository.findByUserIdAndId(userId, documentId);
    if (foundDocuments.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specified document was not found. ");
    }
    repository.delete(foundDocuments.get(0)); // documentId is unique
    return ResponseEntity.ok().build();
  }
}
