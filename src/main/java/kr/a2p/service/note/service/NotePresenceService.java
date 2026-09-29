package kr.a2p.service.note.service;

import kr.a2p.service.note.domain.NoteViewer;
import kr.a2p.service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotePresenceService {
  private final SimpMessagingTemplate messagingTemplate;
  private final UserRepository userRepository;
  private final Map<String, Presence> presenceBySession = new ConcurrentHashMap<>();

  public void join(String noteUser, String sessionId, String viewerUser) {
    String normalizedNoteUser = noteUser.trim();
    String normalizedViewerUser = viewerUser.trim();
    Presence previous = presenceBySession.put(
        sessionId,
        new Presence(
            normalizedNoteUser,
            normalizedViewerUser,
            resolveName(normalizedViewerUser)
        )
    );

    if (previous != null && !previous.noteUser().equals(normalizedNoteUser)) {
      publish(previous.noteUser());
    }

    publish(normalizedNoteUser);
  }

  @EventListener
  public void disconnect(SessionDisconnectEvent event) {
    Presence presence = presenceBySession.remove(event.getSessionId());

    if (presence != null) {
      publish(presence.noteUser());
    }
  }

  private void publish(String noteUser) {
    List<NoteViewer> viewers = presenceBySession.values().stream()
        .filter(presence -> presence.noteUser().equals(noteUser))
        .filter(presence -> !presence.viewerUser().equals(noteUser))
        .collect(java.util.stream.Collectors.toMap(
            Presence::viewerUser,
            presence -> new NoteViewer(
                presence.viewerUser(),
                presence.name()
            ),
            (first, ignored) -> first,
            LinkedHashMap::new
        ))
        .values().stream()
        .sorted(Comparator.comparing(NoteViewer::name))
        .toList();

    messagingTemplate.convertAndSend("/channel/note.presence." + noteUser, viewers);
  }

  private String resolveName(String viewerUser) {
    return userRepository.findById(viewerUser)
        .map(user -> user.getName())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + viewerUser));
  }

  private record Presence(String noteUser, String viewerUser, String name) {
  }
}
