package ru.kolaer.server.webportal.mvc.model.entities.other;

import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.mvp.model.kolaerweb.CounterBase;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 25.08.2016.
 */
@Entity
@Table(name = "counters")
public class CounterDecorator implements Counter {
    private Counter counter;

    public CounterDecorator(Counter counter) {
        this.counter = counter;
    }

    public CounterDecorator() {
        this(new CounterBase());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "counters.seq")
    @Column(name = "id")
    public Integer getId() {
        return this.counter.getId();
    }

    @Override
    public void setId(Integer id) {
        this.counter.setId(id);
    }

    @Column(name = "counter_start", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStart() {
        return this.counter.getStart();
    }

    @Override
    public void setStart(Date start) {
        this.counter.setStart(start);
    }

    @Column(name = "counter_end", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEnd() {
        return this.counter.getEnd();
    }

    @Override
    public void setEnd(Date end) {
        this.counter.setEnd(end);
    }

    @Column(name = "title")
    public String getTitle() {
        return this.counter.getTitle();
    }

    @Override
    public void setTitle(String title) {
        this.counter.setTitle(title);
    }

    @Column(name = "description")
    public String getDescription() {
        return this.counter.getDescription();
    }

    @Override
    public void setDescription(String description) {
        this.counter.setDescription(description);
    }
}
