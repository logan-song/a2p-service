package kr.a2p.service.note.controller;

import kr.a2p.service.note.service.NoteFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/note")
public class NoteWebController {
  private final NoteFacade noteFacade;

  public NoteWebController(NoteFacade noteFacade) {
    this.noteFacade = noteFacade;
  }

  @GetMapping("/{user}")
  public String index(@PathVariable String user, Model model) {
    model.addAttribute("data", noteFacade.noteIndex(user));
    return "note/index";
  }

  @GetMapping("/setup")
  public String setup(Model model) {
    model.addAttribute("data", noteFacade.setupIndex());
    return "note/setup";
  }
}
