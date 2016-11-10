package ru.kolaer.server.webportal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.mvp.model.kolaerweb.CounterBase;
import ru.kolaer.server.webportal.config.SpringContext;
import ru.kolaer.server.webportal.config.SpringSecurityConfig;
import ru.kolaer.server.webportal.mvc.model.dao.CounterDao;
import ru.kolaer.server.webportal.mvc.model.servirces.CounterService;

import java.util.Date;

/**
 * Created by danilovey on 10.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringContext.class, SpringSecurityConfig.class})
@WebAppConfiguration
public class TestSpring {

    @Autowired
    @Qualifier(value = "counterDaoJDBC")
    private CounterDao counterDaoJDBC;

    @Test
    public void testJDBCController() {
        final Counter counter = new CounterBase();
        counter.setEnd(new Date());
        counter.setStart(new Date());
        counter.setTitle("Title");
        counter.setDescription("Des");

        this.counterDaoJDBC.persist(counter);
    }

}
