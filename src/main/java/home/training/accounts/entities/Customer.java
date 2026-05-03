package home.training.accounts.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter @Setter @ToString   @AllArgsConstructor @NoArgsConstructor
public class Customer extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @   Column(name="customer_id")
    private Long customerId;

    private String name;

    private String email;

    @Column(name="mobile_number", nullable = false)
    private String mobileNumber;
}
