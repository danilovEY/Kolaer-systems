package ru.kolaer.server.servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 14.11.2016.
 */
public class WebForwardingServlet extends HttpServlet {
    private final File flag = new File("flag.txt");

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        final HttpServletRequest httpReq = (HttpServletRequest) req;
        final HttpServletResponse httpRes = (HttpServletResponse) res;

        final String param = httpReq.getQueryString();
        String url = httpReq.getRequestURI();

        if(param  != null && !param.trim().isEmpty())
            url += "?" + param;

        httpRes.setHeader("Access-Control-Allow-Origin", httpReq.getHeader("Origin"));
        httpRes.setHeader("Access-Control-Allow-Methods", httpReq.getHeader("Access-Control-Request-Method"));
        httpRes.setHeader("Access-Control-Allow-Headers", httpReq.getHeader("Access-Control-Request-Headers"));
        httpRes.setHeader("Access-Control-Allow-Credentials", "true");

        Enumeration headerNames = httpReq.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            System.out.println("Header Name - " + headerName + ", Value - " + httpReq.getHeader(headerName));
        }

        Enumeration params = httpReq.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            System.out.println("Parameter Name - "+paramName+", Value - "+httpReq.getParameter(paramName));
        }

        Enumeration arts = httpReq.getAttributeNames();
        while(arts.hasMoreElements()){
            String art = (String)arts.nextElement();
            System.out.println("Parameter Name - "+art+", Value - "+httpReq.getAttribute(art));
        }

        if(this.flag.exists()) {
            if (httpReq.getHeader("Access-Control-Request-Method").equals("POST")) {
                this.sendPost("http://danilovey:8080" + url, httpReq, httpRes);
            } else {
                httpRes.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                httpRes.setHeader("Location", "http://danilovey:8080" + url);
            }
        } else {
            if (httpReq.getHeader("Access-Control-Request-Method").equals("POST")) {
                this.sendPost("http://js:8080" + url, httpReq, httpRes);
            } else {
                httpRes.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                httpRes.setHeader("Location", "http://js:8080" + url);
            }
        }
    }

    private void sendPost(String url, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        HttpURLConnection con = null;
        DataOutputStream wr = null;
        InputStream inputStream = null;
        try{
            final URL toJS  = new URL(url);
            con = (HttpURLConnection) toJS.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            final String body = httpReq.getReader().lines().collect(Collectors.joining());
            System.out.println(body);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            final ServletOutputStream writer = httpRes.getOutputStream();
            httpRes.setHeader("Content-Type", "application/json");
            httpRes.setStatus(responseCode);

            inputStream = con.getInputStream();
            int readByte;
            while ((readByte = inputStream.read()) != -1) {
                writer.write(readByte);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(inputStream != null)
                    inputStream.close();
                if(wr != null)
                    wr.close();
                if(con != null)
                    con.disconnect();
            } catch (Throwable ignore) {}
        }
    }
}
