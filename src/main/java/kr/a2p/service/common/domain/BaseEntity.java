package kr.a2p.service.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {
  @LastModifiedDate
  @Column(
      columnDefinition = "DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP"
  )
  protected LocalDateTime updatedAt;

  @CreatedDate
  @Column(
      updatable = false,
      columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP"
  )
  protected LocalDateTime createdAt;

  protected BaseEntity() {
  }

  public abstract T getId();
}
