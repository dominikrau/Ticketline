package at.ac.tuwien.sepm.groupphase.backend.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Table(name = "application_user")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class ApplicationUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, name = "email_address", unique = true)
    private String emailAddress;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String gender;

    /**
     * The roles of the ApplicationUser seperated by ';'
     */
    @Column(nullable = false)
    private String roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;

    @Column
    private boolean blocked;

    @Column(nullable = false)
    private int loginAttempts;

    @ManyToMany(mappedBy = "users")
    private List<MessageEntity> messages;


}
