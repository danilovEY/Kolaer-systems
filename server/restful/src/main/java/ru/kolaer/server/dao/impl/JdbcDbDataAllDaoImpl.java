package ru.kolaer.server.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.dao.DbDataAllDAO;
import ru.kolaer.server.dao.entities.DbDataAll;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 22.08.2016.
 */
@Repository
@Transactional(readOnly = true)
public class JdbcDbDataAllDaoImpl implements DbDataAllDAO {

    @Autowired
    private DataSource dataSource;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public List<DbDataAll> getUserRangeBirthday(Date startData, Date endData) {
        return this.jdbcTemplate.query("SELECT person_number, initials, departament, post, email, vCard, birthday FROM db_data_all WHERE birthday BETWEEN ? AND ?", new Object[]{startData,endData}, (rs, row) -> this.createDbDataAll(rs));
    }

    @Override
    public List<DbDataAll> getUsersByBirthday(Date date) {
        return this.jdbcTemplate.query("SELECT person_number, initials, departament, post, email, vCard, birthday FROM db_data_all WHERE day(birthday) = day(?) and month(birthday) = month(?)", new Object[]{date}, (rs, row) -> this.createDbDataAll(rs));
    }

    @Override
    public List<DbDataAll> getUserBirthdayToday() {
        return this.jdbcTemplate.query("SELECT person_number, initials, departament, post, email, vCard, birthday FROM db_data_all WHERE day(birthday) = day(CURRENT_DATE) and month(birthday) = month(CURRENT_DATE)", (rs, row) -> this.createDbDataAll(rs));
    }

    @Override
    public List<DbDataAll> getUsersByInitials(String initials) {
        return this.jdbcTemplate.query("SELECT person_number, initials, departament, post, email, vCard, birthday FROM db_data_all WHERE initials = ?", new Object[]{initials}, (rs, row) -> this.createDbDataAll(rs));
    }

    @Override
    public int getCountUserBirthday(Date date) {
        return this.jdbcTemplate.queryForObject("SELECT count(birthday) FROM db_data_all WHERE day(birthday) = day(?) and month(birthday) = month(?)", Integer.class, date, date);
    }

    @Override
    public List<DbDataAll> getAll() {
        return this.jdbcTemplate.query("SELECT person_number, initials, departament, post, email, vCard, birthday FROM db_data_all", (rs, row) -> this.createDbDataAll(rs));
    }

    @Override
    public List<DbDataAll> getAllMaxCount(int count) {
        return this.jdbcTemplate.query("SELECT person_number, initials, departament, post, email, vCard, birthday FROM db_data_all LIMIT " + count, (rs, row) -> this.createDbDataAll(rs));
    }

    @Override
    public int getRowCount() {
        return this.jdbcTemplate.queryForObject("SELECT count(person_number) FROM db_data_all", Integer.class);
    }

    private DbDataAll createDbDataAll(ResultSet rs) throws SQLException {
        DbDataAll dbDataAll = new DbDataAll();
        dbDataAll.setBirthday(rs.getDate("birthday"));
        dbDataAll.setInitials(rs.getString("initials"));
        final String[] names = dbDataAll.getInitials().split(" ");
        dbDataAll.setName(names[0]);
        dbDataAll.setSurname(names[1]);
        dbDataAll.setPatronymic(names[2]);
        dbDataAll.setPersonNumber(rs.getInt("person_number"));
        dbDataAll.setDepartament(rs.getString("departament"));
        dbDataAll.setPost(rs.getString("post"));
        dbDataAll.setPhone(rs.getString("phone"));
        dbDataAll.setMobilePhone(rs.getString("mobile_phone"));
        dbDataAll.setEmail(rs.getString("email"));
        dbDataAll.setVCard(rs.getString("vCard"));
        dbDataAll.setCategoryUnit(rs.getString("category_unit"));
        dbDataAll.setLogin(rs.getString("login"));
        dbDataAll.setPassword(rs.getString("password"));
        return dbDataAll;
    }
}
