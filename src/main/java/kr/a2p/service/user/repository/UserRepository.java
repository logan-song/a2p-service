package kr.a2p.service.user.repository;

import kr.a2p.service.user.domain.User;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = String.class)
public interface UserRepository {
  List<User> findAll();

  Optional<User> findById(String id);
}
