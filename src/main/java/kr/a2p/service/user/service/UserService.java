package kr.a2p.service.user.service;

import kr.a2p.service.user.domain.User;
import kr.a2p.service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<User> getUsers() {
    return userRepository.findAll();
  }
}
