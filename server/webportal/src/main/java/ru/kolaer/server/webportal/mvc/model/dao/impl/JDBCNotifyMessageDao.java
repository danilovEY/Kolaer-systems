package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageBase;
import ru.kolaer.server.webportal.mvc.model.dao.NotifyMessageDao;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by danilovey on 18.08.2016.
 */
@Repository("jdbcNotifyMessageDao")
public class JDBCNotifyMessageDao implements NotifyMessageDao {

    @Autowired
    @Qualifier(value = "dataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public NotifyMessage getLastNotifyMessage() {
        return this.findByID(1);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotifyMessage> findAll() {
        return this.jdbcTemplate.query("SELECT id, message FROM notifications", (rs, rowNum) -> {
            final NotifyMessage notifyMessage = new NotifyMessageBase();
            notifyMessage.setId(rs.getInt("id"));
            notifyMessage.setMessage(rs.getString("message"));
            return notifyMessage;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public NotifyMessage findByID(Integer id) {
        List<NotifyMessage> query = this.jdbcTemplate.query("SELECT id, message FROM notifications WHERE id = " + id, (rs, rowNum) -> {
            final NotifyMessage notifyMessage = new NotifyMessageBase();
            notifyMessage.setId(rs.getInt("id"));
            notifyMessage.setMessage(rs.getString("message"));
            return notifyMessage;
        });
        return query.isEmpty() ? null : query.get(0);
    }

    @Override
    @Transactional
    public void persist(NotifyMessage obj) {
        this.jdbcTemplate.update("INSERT INTO notifications (message) VALUES (?)", obj.getMessage());
    }

    @Override
    @Transactional
    public void delete(NotifyMessage obj) {
        this.jdbcTemplate.update("DELETE notifications WHERE id = ?", obj.getId());
    }

    @Override
    public void delete(List<NotifyMessage> objs) {

    }

    @Override
    @Transactional
    public void update(NotifyMessage obj) {
        this.jdbcTemplate.update("UPDATE notifications SET message = ? WHERE id = ?", obj.getMessage(), obj.getId());
    }

    @Override
    public void update(List<NotifyMessage> objs) {

    }
}
