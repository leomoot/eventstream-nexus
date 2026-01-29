package nl.leomoot.eventstreamnexus.domain.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing a client registered in the system.
 */
@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false)
    private Instant createdAt;

    /**
     * Required by JPA for proxy instantiation.
     */
    protected ClientEntity() {
    }

    /**
     * Creates a new client entity with the provided attributes.
     *
     * @param name display name of the client
     * @param email unique email used to contact the client
     * @param createdAt timestamp when the client record was created
     */
    public ClientEntity(String name, String email, Instant createdAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    /**
     * @return unique identifier assigned by the database
     */
    public Long getId() {
        return id;
    }

    /**
     * @return human-readable name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the stored client name.
     *
     * @param name new name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return unique email address of the client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the stored client email.
     *
     * @param email new email value
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return creation timestamp of the client record
     */
    public Instant getCreatedAt() {
        return createdAt;
    }
}
