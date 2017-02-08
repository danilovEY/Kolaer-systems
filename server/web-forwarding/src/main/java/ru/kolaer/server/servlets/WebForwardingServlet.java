package ru.kolaer.server.servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 14.11.2016.
 */
public class WebForwardingServlet extends HttpServlet {
    private static final String PROD_SERVER = "prod";
    private static final String PROD_BACKUP_SERVER = "prod_backup";
    private static final Map<String, String> mapServers = new HashMap<>();
    private final File flag = new File("flag.txt");

    static {
        try (InputStream resource = WebForwardingServlet.class.getResourceAsStream("/servers.properties")) {
            mapServers.putAll(new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().map(line -> line.split("="))
                    .filter(elem -> elem.length==2)
                    .collect(Collectors.toMap(e -> e[0], e -> e[1])));

        } catch (IOException e) {
            mapServers.clear();
            mapServers.put(PROD_SERVER, "http://js:8080");
            mapServers.put(PROD_BACKUP_SERVER, "http://danilovey:8080");
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
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
            con.setDoOutput(true);

            final Enumeration<String> headerNames = httpReq.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                con.setRequestProperty(headerName, httpReq.getHeader(headerName));
            }

            int responseCode = con.getResponseCode();

            final ServletOutputStream writer = httpRes.getOutputStream();
            con.getHeaderFields().forEach((k,v) -> {
                if(k != null)
                    httpRes.addHeader(k, v.stream().collect(Collectors.joining(";")));
            });

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
            con.setDoOutput(true);

            final Enumeration<String> headerNames = httpReq.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                con.setRequestProperty(headerName, httpReq.getHeader(headerName));
            }

            String body = null;
            ServletInputStream inputStreamForRequest = httpReq.getInputStream();
            if(inputStreamForRequest.isReady()) {
                body = new BufferedReader(new InputStreamReader(inputStreamForRequest))
                        .lines().collect(Collectors.joining("\r\n"));
            }

            if(body == null || body.trim().isEmpty())
                body = "{}";

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            final ServletOutputStream writer = httpRes.getOutputStream();
            con.getHeaderFields().forEach((k,v) -> {
                if(k != null)
                    httpRes.addHeader(k, v.stream().collect(Collectors.joining(";")));
            });
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
