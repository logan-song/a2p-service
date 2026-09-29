package kr.a2p.service.note.service;

import jakarta.transaction.Transactional;
import kr.a2p.service.common.error.A2pExceptionFactory;
import kr.a2p.service.note.domain.Note;
import kr.a2p.service.note.model.response.NotePageResponse;
import kr.a2p.service.note.model.response.NoteResponse;
import kr.a2p.service.user.domain.User;
import kr.a2p.service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteFacade {
  private final NoteService noteService;
  private final UserService userService;

  public Note getNote(String user) {
    return noteService.getNote(user);
  }

  public NotePageResponse noteIndex(String username) {
    List<User> users = userService.getUsers();
    User user = users.stream()
        .filter(u -> u.getId().equals(username))
        .findFirst()
        .orElseThrow(() -> A2pExceptionFactory.of(
            HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", null));
    NoteResponse note = NoteResponse.from(getNote(username), user);
    return new NotePageResponse(users, note);
  }

  public NotePageResponse setupIndex() {
    List<User> users = userService.getUsers();
    return new NotePageResponse(users, null);
  }

  @Transactional
  public Note save(String user, String content) {
    Note note = getNote(user);
    note.setContent(content);
    return noteService.save(note);
  }
}
