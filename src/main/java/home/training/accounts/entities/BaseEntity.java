package home.training.accounts.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
//tells jpa MappedSuperclass is NOT a table, but its fields should be inherited by entities
@EntityListeners(AuditingEntityListener.class) //to enable jpa to provide us with audit field values from system and
//class: AuditAwareImpl(later we use spring security to get login user)
public class BaseEntity {

	@CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

	@CreatedBy
    @Column(updatable = false)
    private String createdBy;

	@LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

	@LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;

}
