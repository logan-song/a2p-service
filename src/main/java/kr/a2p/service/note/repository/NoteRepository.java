package kr.a2p.service.note.repository;

import kr.a2p.service.note.domain.Note;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = Note.class, idClass = Long.class)
public interface NoteRepository {
  Note save(Note note);

  Optional<Note> findByUser(String user);
}
