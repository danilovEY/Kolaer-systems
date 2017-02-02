package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.ToString;
import org.hibernate.annotations.Immutable;
import ru.kolaer.api.mvp.model.kolaerweb.PostEntity;
import ru.kolaer.api.mvp.model.kolaerweb.PostEntityBase;

import javax.persistence.*;

/**
 * Created by danilovey on 24.01.2017.
 */
@Entity
@Table(name = "post")
@ToString
public class PostEntityDecorator implements PostEntity {
    private PostEntity postEntity;

    public PostEntityDecorator() {
        this(new PostEntityBase());
    }

    public PostEntityDecorator(PostEntity postEntity) {
        this.postEntity = postEntity;
    }

    @Id
    @Column(name = "id", length = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post.seq")
    public Integer getId() {
        return this.postEntity.getId();
    }

    @Override
    public void setId(Integer id) {
        this.postEntity.setId(id);
    }

    @Column(name = "name")
    public String getName() {
        return this.postEntity.getName();
    }

    @Override
    public void setName(String name) {
        this.postEntity.setName(name);
    }

    @Column(name = "abbreviated_name", length = 50)
    public String getAbbreviatedName() {
        return this.postEntity.getAbbreviatedName();
    }

    @Override
    public void setAbbreviatedName(String abbreviatedName) {
        this.postEntity.setAbbreviatedName(abbreviatedName);
    }

    @Column(name = "type_rang", length = 10)
    public String getTypeRang() {
        return this.postEntity.getTypeRang();
    }

    @Override
    public void setTypeRang(String typeRang) {
        this.postEntity.setTypeRang(typeRang);
    }

    @Column(name = "rang", length = 2)
    public Integer getRang() {
        return this.postEntity.getRang();
    }

    @Override
    public void setRang(Integer rang) {
        this.postEntity.setRang(rang);
    }
}
