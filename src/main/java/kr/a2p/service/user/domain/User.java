package kr.a2p.service.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import kr.a2p.service.common.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class User extends BaseEntity<String> {
  @Id
  @Column(columnDefinition = "VARCHAR(5)")
  private String id;

  @Setter
  @Column(columnDefinition = "VARCHAR(10)")
  private String name;
}
