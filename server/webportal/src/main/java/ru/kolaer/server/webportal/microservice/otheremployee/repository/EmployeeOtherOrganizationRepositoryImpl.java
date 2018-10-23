package ru.kolaer.server.webportal.microservice.otheremployee.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;
import ru.kolaer.server.webportal.microservice.employee.EmployeeOtherOrganizationEntity;
import ru.kolaer.server.webportal.microservice.employee.repository.EmployeeOtherOrganizationRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository(value = "employeeOtherOrganizationDao")
public class EmployeeOtherOrganizationRepositoryImpl extends AbstractDefaultRepository<EmployeeOtherOrganizationEntity> implements EmployeeOtherOrganizationRepository {
	
	protected EmployeeOtherOrganizationRepositoryImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EmployeeOtherOrganizationEntity.class);
	}

	@Override
	public List<EmployeeOtherOrganizationEntity> getAllMaxCount(final int count) {
		return getSession().createQuery("FROM " + getEntityName(), getEntityClass())
				.setMaxResults(count)
				.list();
	}

	@Override
	public int getRowCount() {
		return getSession().createQuery("SELECT count(id) FROM " + getEntityName(), Long.class)
				.uniqueResult()
				.intValue();
	}

	@Override
	public List<EmployeeOtherOrganizationEntity> getUserRangeBirthday(final Date startDate, final Date endDate) {
		return getSession()
				.createQuery("FROM " + getEntityName() + " t where t.birthday BETWEEN :startDate AND :endDate", getEntityClass())
	            .setParameter("startDate", startDate)
	            .setParameter("endDate", endDate)
	            .list();
	}
	
	@Override
	public List<EmployeeOtherOrganizationEntity> getUsersByBirthday(final Date date) {
		return getSession()
				.createQuery("FROM " + getEntityName() +
								" t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)",
						getEntityClass())
	            .setParameter("date", date)
	            .list();
	}

	@Override
	public List<EmployeeOtherOrganizationEntity> getUserBirthdayToday() {
		return getSession()
				.createQuery("FROM " +
								getEntityName() +
								" t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)",
						getEntityClass())
	            .list();
	}

	@Override
	public int getCountUserBirthday(final Date date) {
		return getSession()
				.createQuery("SELECT count(t.id) FROM " + getEntityName() + " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", Long.class)
				.setParameter("date", date)
				.uniqueResult()
				.intValue();
	}

	@Override
	public List<EmployeeOtherOrganizationEntity> getUsersByBirthdayAndOrg(Date date, String organization) {
		return getSession()
				.createQuery("SELECT t FROM " +
								getEntityName() +
								" t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)",
						getEntityClass())
				.setParameter("org", organization)
				.setParameter("date", date)
	            .list();
	}

	@Override
	public int getCountUserBirthdayAndOrg(Date date, String organization) {
		return getSession()
				.createQuery("SELECT count(t.id) FROM " +
								getEntityName() +
								" t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)",
				Long.class)
				.setParameter("org", organization)
				.setParameter("date", date)
				.uniqueResult()
				.intValue();
	}

	@Override
	public List<EmployeeOtherOrganizationEntity> getUsersByInitials(String initials) {
		if(initials == null || initials.isEmpty())
			return Collections.emptyList();
			
		return getSession()
				.createQuery("FROM " + getEntityName() + " t where t.initials like :initials", getEntityClass())
				.setParameter("initials", "%" + initials + "%")
				.list();
	}

}
