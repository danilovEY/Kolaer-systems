package ru.kolaer.server.contact.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.placement.model.entity.PlacementEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contact")
@Data
public class ContactEntity extends DefaultEntity {

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
