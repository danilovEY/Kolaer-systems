package ru.kolaer.server.webportal.mvc.model.entities;

import javax.persistence.*;

/**
 * Created by danilovey on 21.07.2016.
 */
@Entity
@Table(name = "db_post", catalog = "kolaer_base")
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
