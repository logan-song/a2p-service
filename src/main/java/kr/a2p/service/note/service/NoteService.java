package kr.a2p.service.note.service;

import kr.a2p.service.common.error.A2pExceptionFactory;
import kr.a2p.service.note.domain.Note;
import kr.a2p.service.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteService {
  private final NoteRepository noteRepository;

  public Note getNote(String user) {
    return noteRepository.findByUser(user)
        .orElseThrow(() -> A2pExceptionFactory.of(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", null));
  }

  public Note save(Note note) {
    return noteRepository.save(note);
  }
}
