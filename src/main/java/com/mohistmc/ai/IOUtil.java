package com.mohistmc.ai;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class IOUtil {

    /**
     * @return inputStream
     * @Title: getInputStream
     * @Description: TODO 获取网络连接的InputStream
     */
    public static InputStream getInputStream(String s) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection httpurlconn;
        try {
            URL url = URI.create(s).toURL();
            if (url != null) {
                httpurlconn = (HttpURLConnection) url.openConnection();
                //设置连接超时时间
                httpurlconn.setConnectTimeout(3000);
                //表示使用GET方式请求
                httpurlconn.setRequestMethod("GET");
                httpurlconn.setRequestProperty("Authorization", "authorization");
                int responsecode = httpurlconn.getResponseCode();
                if (responsecode == 200) {
                    //从服务返回一个输入流
                    inputStream = httpurlconn.getInputStream();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static boolean closeStream(Closeable... pSteams) {
        boolean pHasError = false;
        for (Closeable sCloseable : pSteams) {
            if (sCloseable != null) {
                try {
                    sCloseable.close();
                } catch (Exception exp) {
                    pHasError = true;
                }
            }
        }

        return !pHasError;
    }

    public static boolean closeStream(AutoCloseable... pConns) {
        boolean pHasError = false;
        for (AutoCloseable sCloseable : pConns) {
            if (sCloseable != null) {
                try {
                    sCloseable.close();
                } catch (Exception exp) {
                    pHasError = true;
                }
            }
        }

        return !pHasError;
    }

    public static long copy(InputStream pIPStream, OutputStream pOPStream) throws IOException {
        int copyedCount = 0, readCount;
        byte[] tBuff = new byte[4096];
        while ((readCount = pIPStream.read(tBuff)) != -1) {
            pOPStream.write(tBuff, 0, readCount);
            copyedCount += readCount;
        }
        return copyedCount;
    }

    public static String readContent(InputStream pIPStream) throws IOException {
        return IOUtil.readContent(new InputStreamReader(pIPStream, StandardCharsets.UTF_8));
    }

    public static String readContent(InputStreamReader pIPSReader) throws IOException {
        int readCount;
        char[] tBuff = new char[4096];
        StringBuilder tSB = new StringBuilder();
        while ((readCount = pIPSReader.read(tBuff)) != -1) {
            tSB.append(tBuff, 0, readCount);
        }
        return tSB.toString();
    }

    public static byte[] readData(InputStream pIStream) throws IOException {
        ByteArrayOutputStream tBAOStream = new ByteArrayOutputStream();
        IOUtil.copy(pIStream, tBAOStream);
        return tBAOStream.toByteArray();
    }

    public static void throwException(Throwable exception) throws Throwable {
        throwException0(exception);
    }

    private static <T extends Throwable> void throwException0(Throwable exception) throws Throwable {
        if (exception != null) {
            throw exception;
        }
    }
}