package ru.kolaer.server.webportal.mvc.model.entities.contact;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.placement.PlacementEntity;

import javax.persistence.*;

@Entity
@Table(name = "contact")
@Data
public class ContactEntity implements BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "type")
    private ContactType type;

    @Column(name = "work_phone_number")
    private String workPhoneNumber;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "pager")
    private String pager;

    @Column(name = "place_id")
    private Long placementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", insertable=false, updatable=false)
    private PlacementEntity placement;
}
