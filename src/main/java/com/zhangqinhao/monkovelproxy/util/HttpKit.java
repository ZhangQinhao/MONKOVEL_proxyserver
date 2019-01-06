package com.zhangqinhao.monkovelproxy.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

public class HttpKit {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String CHARSET = "UTF-8";
    private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
    private static final HttpKit.TrustAnyHostnameVerifier trustAnyHostnameVerifier = new HttpKit().new TrustAnyHostnameVerifier();
    private final static Logger logger = LogManager.getLogger(HttpKit.class.getName());

    private HttpKit() {
    }

    private static SSLSocketFactory initSSLSocketFactory() {
        try {
            TrustManager[] tm = new TrustManager[]{new HttpKit().new TrustAnyTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[])null, tm, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
            ((HttpsURLConnection)conn).setHostnameVerifier(trustAnyHostnameVerifier);
        }

        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(19000);
        conn.setReadTimeout(19000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (headers != null && !headers.isEmpty()) {
            Iterator var6 = headers.entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var6.next();
                conn.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
            }
        }

        return conn;
    }

    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers){
        return get(CHARSET,url,queryParas,headers);
    }

    public static String get(String charset,String url, Map<String, String> queryParas, Map<String, String> headers) {
        HttpURLConnection conn = null;
        String var6;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), "GET", headers);
            conn.connect();
            var6 = readResponseString(conn,charset);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return var6;
    }

    public static String getByAutoDecode(String url, Map<String, String> queryParas, Map<String, String> headers) {
        HttpURLConnection conn = null;
        String var6;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), "GET", headers);
            conn.connect();
            var6 = readResponseStringByAutoDecode(conn);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return var6;
    }

    private static String getHtmlCharset(String data) throws Exception{
        Document doc = Jsoup.parse(data);
        Elements metaContents = doc.select("meta[content*='charset=']");
        if(metaContents!=null&&metaContents.size()>0){
            String contentValue = metaContents.get(0).attr("content");
            String[] values = contentValue.split(";");
            for(String value:values){
                if(value.contains("charset=")){
                    return value.replace("charset=","").trim().toLowerCase();
                }
            }
        }
        Elements metaCharsets = doc.select("meta[charset]");
        if(metaCharsets!=null&&metaCharsets.size()>0){
            String value = metaCharsets.get(0).attr("charset");
            return value.toLowerCase();
        }
        return null;
    }

    public static String get(String url, Map<String, String> queryParas) {
        return get(CHARSET,url, queryParas);
    }

    public static String get(String charset,String url, Map<String, String> queryParas) {
        return get(charset,url, queryParas, null);
    }

    public static String get(String url) {
        return get(CHARSET,url);
    }

    public static String get(String charset,String url) {
        return get(charset,url, null, null);
    }

    public static String post(String charset,String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
        HttpURLConnection conn = null;

        String var7;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), "POST", headers);
            conn.connect();
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes(charset));
            out.flush();
            out.close();
            var7 = readResponseString(conn,charset);
        } catch (Exception var10) {
            throw new RuntimeException(var10);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return var7;
    }

    public static String postByAutoDecode(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
        HttpURLConnection conn = null;
        String var7;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), "POST", headers);
            conn.connect();
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes("utf-8"));
            out.flush();
            out.close();
            var7 = readResponseStringByAutoDecode(conn);
        } catch (Exception var10) {
            throw new RuntimeException(var10);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return var7;
    }

    public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers){
        return post(CHARSET,url,queryParas,data,headers);
    }

    public static String post(String url, Map<String, String> queryParas, String data) {
        return post(CHARSET,url, queryParas, data);
    }

    public static String post(String charset,String url, Map<String, String> queryParas, String data){
        return post(charset,url, queryParas, data, (Map)null);
    }

    public static String post(String url, String data, Map<String, String> headers) {
        return post(CHARSET,url, data, headers);
    }

    public static String post(String charset,String url, String data, Map<String, String> headers){
        return post(charset,url, (Map)null, data, headers);
    }

    public static String post(String url, String data) {
        return post(CHARSET,url, data);
    }

    public static String post(String charset,String url,String data){
        return post(CHARSET,url, null, data, null);
    }

    private static String readResponseString(HttpURLConnection conn,String charset) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;

        try {
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
            String line = null;

            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String var6 = sb.toString();
            return var6;
        } catch (Exception var13) {
            throw new RuntimeException(var13);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var12) {
                    logger.error(var12.getMessage(), var12);
                }
            }

        }
    }

    private static String readResponseStringByAutoDecode(HttpURLConnection conn) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream inputStream = null;

        try {
            inputStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            String var6 = result.toString("utf-8");
            if(var6!=null && var6.length()>0){
                try{
                    String charset = getHtmlCharset(var6);
                    if(charset!=null && charset.length()>0 && !charset.equalsIgnoreCase("utf-8")){
                        var6 = result.toString(charset);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return var6;
        } catch (Exception var13) {
            throw new RuntimeException(var13);
        } finally {
            if(result!=null){
                try {
                    result.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var12) {
                    logger.error(var12.getMessage(), var12);
                }
            }

        }
    }

    private static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
        if (queryParas != null && !queryParas.isEmpty()) {
            StringBuilder sb = new StringBuilder(url);
            boolean isFirst;
            if (url.indexOf("?") == -1) {
                isFirst = true;
                sb.append("?");
            } else {
                isFirst = false;
            }

            String key;
            String value;
            for(Iterator var5 = queryParas.entrySet().iterator(); var5.hasNext(); sb.append(key).append("=").append(value)) {
                Map.Entry<String, String> entry = (Map.Entry)var5.next();
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append("&");
                }

                key = (String)entry.getKey();
                value = (String)entry.getValue();
                if (StringUtil.isNotEmpty(value)) {
                    try {
                        value = URLEncoder.encode(value, CHARSET);
                    } catch (UnsupportedEncodingException var9) {
                        throw new RuntimeException(var9);
                    }
                }
            }

            return sb.toString();
        } else {
            return url;
        }
    }

    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;

        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            String line = null;

            while((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }

            String var5 = result.toString();
            return var5;
        } catch (IOException var12) {
            throw new RuntimeException(var12);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var11) {
                    logger.error(var11.getMessage(),var11);
                }
            }

        }
    }

    /** @deprecated */
    @Deprecated
    public static String readIncommingRequestData(HttpServletRequest request) {
        return readData(request);
    }

    private class TrustAnyHostnameVerifier implements HostnameVerifier {
        private TrustAnyHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private class TrustAnyTrustManager implements X509TrustManager {
        private TrustAnyTrustManager() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }
}
