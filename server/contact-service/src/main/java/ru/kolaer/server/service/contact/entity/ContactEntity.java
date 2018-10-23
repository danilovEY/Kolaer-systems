package ru.kolaer.server.service.contact.entity;

import lombok.Data;
import ru.kolaer.server.webportal.common.entities.BaseEntity;
import ru.kolaer.server.service.contact.ContactType;
import placement.entity.PlacementEntity;

import javax.persistence.*;

@Entity
@Table(name = "ru/kolaer/server/service/contact")
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
