package ru.kolaer.server.contact.model.entity;


import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

//@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ContactEntity.class)
public class ContactEntity_ {

    public static volatile SingularAttribute<ContactEntity, Long> id;
    public static volatile SingularAttribute<ContactEntity, Long> employeeId;
    public static volatile SingularAttribute<ContactEntity, String> email;
    public static volatile SingularAttribute<ContactEntity, ContactType> type;
    public static volatile SingularAttribute<ContactEntity, String> workPhoneNumber;
    public static volatile SingularAttribute<ContactEntity, String> mobilePhoneNumber;
    public static volatile SingularAttribute<ContactEntity, String> pager;
    public static volatile SingularAttribute<ContactEntity, Long> placementId;

}
