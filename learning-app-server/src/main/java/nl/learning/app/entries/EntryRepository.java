package nl.learning.app.entries;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends CrudRepository<Entry, Long> {
}
