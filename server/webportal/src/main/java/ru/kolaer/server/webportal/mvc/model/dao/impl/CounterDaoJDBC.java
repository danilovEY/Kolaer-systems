package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.mvp.model.kolaerweb.CounterBase;
import ru.kolaer.server.webportal.mvc.model.dao.CounterDao;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by danilovey on 09.11.2016.
 */
@Repository(value = "counterDaoJDBC")
public class CounterDaoJDBC implements CounterDao {

    @Autowired
    @Qualifier(value = "jdbcTemplateOrigin")
    private JdbcTemplate jdbcTemplateOrigin;

    @Override
    @Transactional(readOnly = true)
    public List<Counter> findAll() {
        return this.jdbcTemplateOrigin.query("SELECT * FROM counters", new CounterMapping());
    }

    @Override
    public Counter findByID(int id) {
        return this.jdbcTemplateOrigin.queryForObject("SELECT * FROM counters WHERE id = ?",new Object[] {id}, new CounterMapping());
    }

    @Override
    public void persist(Counter obj) {
        this.jdbcTemplateOrigin.update("INSERT INTO counters(title, counter_start, counter_end, description) VALUES (?,?,?,?)",
                obj.getTitle(),obj.getStart(),obj.getEnd(),obj.getDescription());
    }

    @Override
    public void delete(Counter obj) {

    }

    @Override
    public void delete(@NotNull(message = "Объект NULL!") List<Counter> objs) {

    }

    @Override
    public void update(Counter obj) {

    }

    @Override
    public void update(@NotNull(message = "Объект NULL!") List<Counter> objs) {

    }
}

class CounterMapping implements RowMapper<Counter> {

    @Override
    public Counter mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Counter counter = new CounterBase();
        counter.setId(rs.getInt("id"));
        counter.setTitle(rs.getString("title"));
        counter.setStart(rs.getDate("start"));
        counter.setEnd(rs.getDate("end"));
        counter.setDescription(rs.getString("description"));
        return counter;
    }
}
