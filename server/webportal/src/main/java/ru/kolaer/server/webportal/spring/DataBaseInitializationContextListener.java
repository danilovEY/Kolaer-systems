package ru.kolaer.server.webportal.spring;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.server.webportal.mvc.model.dao.impl.DataBaseInitialization;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralDepartamentEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

/**
 * Created by danilovey on 12.09.2016.
 */
@Component
public class DataBaseInitializationContextListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(DataBaseInitializationContextListener.class);

    @Autowired
    private DataBaseInitialization dataBaseInitialization;

    @Autowired
    private UrlPathService urlPathService;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibGen;

    private boolean isInit = false;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!isInit) {
            urlPathService.clear();
            dataBaseInitialization.updateDataBase();
            if (hibGen.equals("create")) {
                dataBaseInitialization.initDB();
            }
            isInit = true;
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
