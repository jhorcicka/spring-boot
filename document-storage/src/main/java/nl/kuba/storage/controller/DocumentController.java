package nl.kuba.storage.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import nl.kuba.storage.model.Document;
import nl.kuba.storage.repository.DocumentRepository;
import nl.kuba.storage.service.FileStorageService;

@RestController
@RequestMapping(value = "/documents")
public class DocumentController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);
  private static final String PATH_DOCUMENT_LIST = "/user/{userId}";
  private static final String PATH_DOCUMENT = "{documentId}/user/{userId}";
  private static final String PATH_DOWNLOAD_FILE = "{documentId}/user/{userId}/download";
  private static final String PATH_SAVE_DOCUMENT = PATH_DOCUMENT_LIST;
  private static final String PATH_UPLOAD_FILE = "{documentId}/user/{userId}/upload";
  private static final String PATH_UPDATE_DOCUMENT = PATH_DOCUMENT;
  private static final String PATH_DELETE_DOCUMENT = PATH_DOCUMENT;

  @Autowired
  private DocumentRepository repository;

  @Autowired
  private FileStorageService fileStorageService;

  @GetMapping(path = PATH_DOCUMENT_LIST)
  public @ResponseBody
  Iterable<Document> getUserDocumentList(@PathVariable final Long userId) {
    LOGGER.debug("getUserDocumentList: userId={}", userId);
    return repository.findByUserId(userId);
  }

  @GetMapping(path = PATH_DOCUMENT)
  public @ResponseBody
  ResponseEntity<Document> getUserDocument(@PathVariable final Long documentId, @PathVariable final Long userId) {
    LOGGER.debug("getUserDocument: userId={}, documentId={}", userId, documentId);
    final List<Document> foundDocuments = repository.findByUserIdAndId(userId, documentId);
    if (foundDocuments.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok().body(foundDocuments.get(0)); // documentId is unique
    }
  }

  @GetMapping(path = PATH_DOWNLOAD_FILE)
  public @ResponseBody
  ResponseEntity<Resource> downloadUserDocumentFile(@PathVariable final Long documentId, @PathVariable final Long userId)
          throws IOException {
    LOGGER.debug("downloadUserDocument: userId={}, documentId={}", userId, documentId);
    final List<Document> foundDocuments = repository.findByUserIdAndId(userId, documentId);
    if (foundDocuments.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      final File file = fileStorageService.get(foundDocuments.get(0).getRelativeFilePath());
      final InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
      return ResponseEntity
              .ok()
              .header("Content-disposition", "attachment; filename=" + foundDocuments.get(0).getName())
              .contentLength(file.length())
              .contentType(MediaType.APPLICATION_OCTET_STREAM)
              .body(resource);
    }
  }

  @PutMapping(path = PATH_SAVE_DOCUMENT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> saveDocument(@PathVariable final Long userId, @RequestBody final Document document)
          throws URISyntaxException {
    LOGGER.debug("saveDocument: userId={}, documentName={}", userId, document.getName());
    final List<Document> userDocumentsWithSameName = repository.findByUserIdAndName(document.getUserId(), document.getName());
    if (!userDocumentsWithSameName.isEmpty()) {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Provided document name is not unique. ");
    }
    final Document newDocument = repository.save(Document.builder().created(new Date()).userId(document.getUserId())
        .name(document.getName())
        .type(document.getType())
        .notes(document.getNotes())
        .build());
    final URI uri = new URI("/documents/" + newDocument.getId() + "/user/" + document.getUserId());
    return ResponseEntity.created(uri).body(newDocument.getId().toString());
  }

  @PostMapping(path = PATH_UPLOAD_FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> uploadFile(@PathVariable final Long userId,
                                           @PathVariable final Long documentId,
                                           @RequestParam("file") final MultipartFile file) throws IOException {
    LOGGER.debug("uploadFile: userId={}, documentName={}", userId, documentId);
    final List<Document> documentList = repository.findByUserIdAndId(userId, documentId);
    if (documentList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specified document was not found. ");
    } else {
      final Document document = documentList.get(0); // documentId is unique
      final String newRelativePath = fileStorageService.store(file.getInputStream());
      LOGGER.debug("newRelativePath: {}", newRelativePath);
      repository.updateUserDocument(userId, documentId, file.getOriginalFilename(), newRelativePath, document.getNotes());
      return ResponseEntity.ok().build();
    }
  }

  @PostMapping(path = PATH_UPDATE_DOCUMENT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateDocument(@PathVariable final Long documentId, @PathVariable final Long userId, @RequestBody final Document document) {
    LOGGER.debug("updateDocument: documentId={}, userId={}", documentId, userId);
    repository.updateUserDocument(userId, documentId, document.getRelativeFilePath(), document.getName(), document.getNotes());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = PATH_DELETE_DOCUMENT)
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
