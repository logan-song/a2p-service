package kr.a2p.service.note.model.response;

import kr.a2p.service.note.domain.Note;
import kr.a2p.service.user.domain.User;
import lombok.Getter;

@Getter
public class NoteResponse {
  private String content;
  private User user;

  public static NoteResponse from(Note note, User user) {
    NoteResponse noteResponse = new NoteResponse();
    noteResponse.content = note.getContent();
    noteResponse.user = user;
    return noteResponse;
  }
}
