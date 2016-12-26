package ru.kolaer.server.servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 14.11.2016.
 */
public class WebForwardingServlet extends HttpServlet {
    private final String PROD_SERVER = "prod";
    private final String PROD_BACKUP_SERVER = "prod_backup";
    private final Map<String, String> mapServers = new HashMap<>();
    private final File flag = new File("flag.txt");

    @Override
    public void init() throws ServletException {
        super.init();
        try (InputStream resource = this.getClass().getResourceAsStream("./servers.properties")) {
            this.mapServers.putAll(new BufferedReader(new InputStreamReader(resource,
                            StandardCharsets.UTF_8)).lines().map(line -> line.split("="))
                            .filter(elem -> elem.length==2)
                            .collect(Collectors.toMap(e -> e[0], e -> e[1])));

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(this.mapServers.size() < 2) {
            this.mapServers.clear();
            this.mapServers.put(this.PROD_SERVER, "http://js:8080");
            this.mapServers.put(this.PROD_BACKUP_SERVER, "http://danilovey:8080");
        }
    }

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
        httpRes.setHeader("Access-Control-Max-Age", "86400");

        if(this.flag.exists()) {
            if (httpReq.getMethod().equals("POST")) {
                this.sendPost(this.mapServers.get(this.PROD_BACKUP_SERVER) + url, httpReq, httpRes);
            } else {
                if(httpReq.getMethod().equals("GET")) {
                    sendGet(this.mapServers.get(this.PROD_BACKUP_SERVER) + url, httpReq, httpRes);
                }
            }
        } else {
            if (httpReq.getMethod().equals("POST")) {
                this.sendPost(this.mapServers.get(this.PROD_SERVER) + url, httpReq, httpRes);
            } else {
                if(httpReq.getMethod().equals("GET")) {
                    this.sendGet(this.mapServers.get(this.PROD_SERVER) + url, httpReq, httpRes);
                }
            }
        }
    }

    private void sendGet(String url, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        HttpURLConnection con = null;
        InputStream inputStream = null;
        try{
            final URL toJS  = new URL(url);
            con = (HttpURLConnection) toJS.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Accept","*");
            con.setDoOutput(true);
            if(httpReq.getHeader("X-Token") != null) {
                con.setRequestProperty("X-Token", httpReq.getHeader("X-Token"));
            }
            int responseCode = con.getResponseCode();

            final ServletOutputStream writer = httpRes.getOutputStream();
            httpRes.setHeader("Content-Type", "application/json");
            httpRes.setStatus(responseCode);
            inputStream = responseCode == 200 ? con.getInputStream() : con.getErrorStream();
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
            con.setRequestProperty("Accept","*");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if(httpReq.getHeader("X-Token") != null) {
                con.setRequestProperty("X-Token", httpReq.getHeader("X-Token"));
            }

            String body = null;
            ServletInputStream inputStream1 = httpReq.getInputStream();
            if(inputStream1.isReady()) {
                body = new BufferedReader(new InputStreamReader(inputStream1))
                        .lines().collect(Collectors.joining("\n"));
            }

            if(body == null || body.trim().isEmpty())
                body = "{}";

            System.out.println("Body: " + body);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            final ServletOutputStream writer = httpRes.getOutputStream();
            httpRes.setHeader("Content-Type", "application/json");
            httpRes.setStatus(responseCode);
            inputStream = responseCode == 200 ? con.getInputStream() : con.getErrorStream();
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
