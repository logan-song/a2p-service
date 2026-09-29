package kr.a2p.service.note.controller;

import kr.a2p.service.note.domain.NoteMessage;
import kr.a2p.service.note.service.NotePresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NoteSocketController {
  private final NotePresenceService notePresenceService;

  @MessageMapping("/note.update.{user}")
  @SendTo("/channel/note.{user}")
  public NoteMessage update(
      @DestinationVariable String user,
      NoteMessage message
  ) {
    return message;
  }

  @MessageMapping("/note.presence.join.{user}")
  public void join(
      @DestinationVariable String user,
      @Payload String viewerUser,
      SimpMessageHeaderAccessor headers
  ) {
    notePresenceService.join(user, headers.getSessionId(), viewerUser);
  }

}
