package ru.kolaer.server.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by danilovey on 14.11.2016.
 */
public class WebForwardingServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(WebForwardingServlet.class);
    private final File flag = new File("flag.txt");

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        final HttpServletRequest httpReq = (HttpServletRequest) req;

        final String param = httpReq.getQueryString();
        String url = httpReq.getRequestURI();

        if(param  != null && !param.isEmpty())
            url += "?" + param;

        logger.info("URL: {}",url);

        final HttpServletResponse httpRes = (HttpServletResponse) res;
        if(this.flag.exists()) {
            httpRes.sendRedirect("http://danilovey:8080" + url);
        } else {
            httpRes.sendRedirect("http://js:8080" + url);
        }

        super.service(req, res);
    }

}
