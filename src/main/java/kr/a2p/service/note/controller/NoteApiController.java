package kr.a2p.service.note.controller;

import kr.a2p.service.note.model.request.NoteRequest;
import kr.a2p.service.note.model.response.NoteResponse;
import kr.a2p.service.note.service.NoteFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/note/api")
@RequiredArgsConstructor
public class NoteApiController {
  private final NoteFacade noteFacade;

  @GetMapping("/{user}")
  public ResponseEntity<NoteResponse> callContent(@PathVariable String user) {
    return ResponseEntity.ok(NoteResponse.from(noteFacade.getNote(user), null));
  }

  @PutMapping("/{user}")
  public ResponseEntity<Void> index(
      @PathVariable String user,
      @RequestBody NoteRequest request
  ) {
    noteFacade.save(user, request.getContent());
    return ResponseEntity.ok().build();
  }
}
