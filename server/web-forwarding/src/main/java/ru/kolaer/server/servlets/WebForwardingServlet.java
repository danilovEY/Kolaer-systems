package ru.kolaer.server.servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Optional;
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
        //httpRes.setHeader("Access-Control-Max-Age", "86400");
        //httpRes.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");


        /*System.out.println("123123: " + httpReq.getHeader("X-Token"));
        Enumeration headerNames = httpReq.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            System.out.println("Header Name - " + headerName + ", Value - " + httpReq.getHeader(headerName));
        }*/

        /*Enumeration params = httpReq.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            System.out.println("Parameter Name - "+paramName+", Value - "+httpReq.getParameter(paramName));
        }*/

        if(this.flag.exists()) {
            if (Optional.ofNullable(httpReq.getHeader("Access-Control-Request-Method"))
                    .orElse(httpReq.getMethod()).equals("POST")) {
                this.sendPost("http://danilovey:8080" + url, httpReq, httpRes);
            } else {
                if(httpReq.getHeader("Access-Control-Request-Headers") == null) {
                    httpRes.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                    httpRes.addHeader("Location", "http://danilovey:8080" + url);
                }
            }
        } else {
            if (httpReq.getHeader("Access-Control-Request-Method") == null
                    && httpReq.getMethod().equals("POST")) {
                this.sendPost("http://js:9090" + url, httpReq, httpRes);
            } else {
                if(httpReq.getHeader("Access-Control-Request-Headers") == null) {
                    sendGet("http://js:9090" + url, httpReq, httpRes);
                //httpRes.sendRedirect("http://js:9090" + url);
                    //httpRes.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                    //httpRes.setHeader("Location", "http://js:9090" + url);
                }
            }
        }
    }

    private void sendGet(String url, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        HttpURLConnection con = null;
        DataOutputStream wr = null;
        InputStream inputStream = null;
        try{
            final URL toJS  = new URL(url);
            con = (HttpURLConnection) toJS.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setDoOutput(false);
            if(httpReq.getHeader("X-Token") != null) {
                con.setRequestProperty("X-Token", httpReq.getHeader("X-Token"));
            }
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
                if(con != null)
                    con.disconnect();
            } catch (Throwable ignore) {}
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
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if(httpReq.getHeader("X-Token") != null) {
                con.setRequestProperty("X-Token", httpReq.getHeader("X-Token"));
                System.out.println(httpReq.getHeader("X-Token"));
            }

            String body = "";
            ServletInputStream inputStream1 = httpReq.getInputStream();
            if(inputStream1.isReady()) {
                body = new BufferedReader(new InputStreamReader(inputStream1))
                        .lines().collect(Collectors.joining("\n"));
            }

            if(body == null || body.trim().isEmpty()) {
                Enumeration arts = httpReq.getAttributeNames();
                while(arts.hasMoreElements()){
                    body = (String)arts.nextElement();
                }
                if(body == null || body.trim().isEmpty())
                    body = "{}";
            }

            System.out.println("Body: " + body);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            final ServletOutputStream writer = httpRes.getOutputStream();
            httpRes.setHeader("Content-Type", "application/json");
            httpRes.setStatus(responseCode);
            if(responseCode == 200) {
                inputStream = con.getInputStream();
                int readByte;
                while ((readByte = inputStream.read()) != -1) {
                    writer.write(readByte);
                }
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
