package kr.a2p.service.note.model.response;

import kr.a2p.service.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotePageResponse {
  private List<User> users;
  private NoteResponse note;
}
