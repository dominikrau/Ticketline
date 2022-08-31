package at.ac.tuwien.sepm.groupphase.backend.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 1, max = 100)
    @Column(nullable = false, name = "name")
    private String name;

    @Length(min = 1, max = 10000)
    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "type")
    private String eventType;

    @Length(min = 1, max = 1000)
    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "is_playing",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private List<ArtistEntity> artists;

    @OneToMany(mappedBy = "event",cascade = CascadeType.MERGE)
    private List<ShowEntity> shows;

}
