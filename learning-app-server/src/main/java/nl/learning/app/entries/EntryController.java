package nl.learning.app.entries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/entries")
@Slf4j
public class EntryController {
  private static final String PATH_LIST = "/";
  private static final String PATH_BY_ID = "/{id}";
  private static final String PATH_SAVE = "/";
  private static final String PATH_DELETE = "/{id}";

  @Autowired
  private EntryRepository repository;

  @GetMapping(path = PATH_LIST)
  public @ResponseBody
  Iterable<Entry> getList() {
    log.debug("getList");
    return repository.findAll();
  }

  @GetMapping(path = PATH_BY_ID)
  public @ResponseBody
  Optional<Entry> getOne(@PathVariable  final Long id) {
    log.debug("getOne: id={}", id);
    return repository.findById(id);
  }

  @PostMapping(path = PATH_SAVE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody
  void save(@RequestBody final Entry entry) {
    log.debug("save: entry={}", entry);
    repository.save(entry);
  }

  @DeleteMapping(path = PATH_DELETE)
  public @ResponseBody
  void delete(@PathVariable final Long id) {
    log.debug("delete: id={}", id);
    final Optional<Entry> entry = repository.findById(id);
    entry.ifPresent(value -> repository.delete(value));
  }
}
