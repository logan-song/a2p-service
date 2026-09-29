package kr.a2p.service.note.domain;

import jakarta.persistence.*;
import kr.a2p.service.common.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(
    indexes = {
        @Index(name = "idx_user", columnList = "user")
    }
)
public class Note extends BaseEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "VARCHAR(5)")
  private String user;

  @Setter
  @Column(columnDefinition = "LONGTEXT", length = 4000)
  private String content;
}
