package org.k2.processmining.storage;


import org.apache.commons.io.IOUtils;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.exception.InternalServerErrorException;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.util.Message;
import org.k2.processmining.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nyq on 2017/6/13.
 */
@Component
public class HDFS implements LogStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(HDFS.class);

    private static final String HDFS_URL = "http://116.56.129.93:50070/webhdfs/v1/AsWeCan/ProcessMining";
    private static final String LISTSTATUS = "op=LISTSTATUS";
    private static final String MKDIRS = "op=MKDIRS";
    private static final String CREATE = "op=CREATE";
    private static final String DELETE = "op=DELETE";
    private static final String OPEN = "op=OPEN";

    private static final String OWNER = "AsWeCan";

    private static int CONNECT_TIMEOUT = 5000;
    private static int READ_TIMEOUT = 30000;

    public HDFS() {
        init();
    }

    private void init() {
        try {
            httpPUT(HDFS_URL + "?" + MKDIRS + "&user.name=" + OWNER);
        }
        catch (IOException e) {
            String msg = "Fail to init file system";
            LOGGER.error(msg, e);
            throw new RuntimeException(msg);
        }
    }

    private String getUploadUrl(AbstractLog log) throws IOException {
        return getLogLocation(log) + "?" + CREATE + "&user.name=" + OWNER + "&overwrite=true";
    }

    private String getDownloadUrl(AbstractLog log) throws IOException {
        return getLogLocation(log) + "?" + OPEN;
    }

    private String getDeleteUrl(AbstractLog log) throws IOException {
        return getLogLocation(log) + "?" + DELETE;
    }

    @Override
    public String getLogLocation(AbstractLog log) throws IOException{
        if (log == null || log.getUserId() == null || log.getId() == null) {
            throw new URLJoinException("Fail to join URL for AbstractLog: " + log);
        }
        return HDFS_URL + "/" + log.getUserId() + "/" + log.getType() + "/" + log.getId();
    }

    @Override
    public void makeDirectoryForUser(User user) throws IOException{
        for (String logTypeName : Util.getLogTypeNames()) {
            String reqUrl = HDFS.HDFS_URL + "/" + user.getId() + "/" + logTypeName + "?" + MKDIRS + "&user.name=" + OWNER;
            if(! httpPUT(reqUrl)) {
                deleteUser(user);
            }
        }
    }

    @Override
    public boolean isExist(AbstractLog log) throws IOException {
        return isExist(getLogLocation(log) + "?" + LISTSTATUS);
    }

    @Override
    public boolean isExist(User user) throws IOException {
        for (String logTypeName : Util.getLogTypeNames()) {
            String reqUrl = HDFS.HDFS_URL + "/" + user.getId() + "/" + logTypeName + "?" + LISTSTATUS;
            if (!isExist(reqUrl)) {
                return false;
            }
        }
        return true;
    }

    private boolean isExist(String url) throws IOException {
        return httpGET(url);
    }

    private boolean httpPUT(String reqUrl) throws IOException {
        return httpReq(reqUrl, "PUT");
    }

    private boolean httpGET(String reqUrl) throws IOException {
        return httpReq(reqUrl, "GET");
    }

    private boolean httpReq(String reqUrl, String method) throws IOException {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.connect();
            return isSuccess(conn.getResponseCode());
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public void deleteUser(User user) throws IOException {
        if (user == null) {
            throw new URLJoinException("User is null");
        }
        String reqUrl = HDFS_URL + "/" + user.getId() + "?" + DELETE + "&recursive=true";
        delete(reqUrl);
    }

    @Override
    public void upload(AbstractLog log, InputStream inputStream) throws IOException{
        String reqUrl = getUploadUrl(log);
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
//           conn.setConnectTimeout(5000);
//	         conn.setReadTimeout(30000);
            IOUtils.copyLarge(inputStream, conn.getOutputStream());
            verifyResCode(conn);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public <T> T upload(AbstractLog log, ProcessOutputStream<T> processOutputStream) throws IOException {
        String reqUrl = getUploadUrl(log);
        System.out.println("upload: "+reqUrl);
        URL url;
        HttpURLConnection conn = null;
        T res;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
//           conn.setConnectTimeout(5000);
//	         conn.setReadTimeout(30000);
            res = processOutputStream.processOutputStream(conn.getOutputStream());
            verifyResCode(conn);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    @Override
    public void download(AbstractLog log, OutputStream outputStream) throws IOException{
        String reqUrl = getDownloadUrl(log);
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.connect();
            IOUtils.copyLarge(conn.getInputStream(), outputStream);
            verifyResCode(conn);
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public <T> T download(AbstractLog log, ProcessInputStream<T> processInputStream) throws IOException {
        String reqUrl = getDownloadUrl(log);
        URL url;
        HttpURLConnection conn = null;
        T res;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.connect();
            res = processInputStream.processInputStream(conn.getInputStream());
            verifyResCode(conn);
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    @Override
    public void delete(AbstractLog log) throws IOException {
        delete(getDeleteUrl(log));
    }

    private void delete(String reqUrl) throws IOException{
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.connect();
            verifyResCode(conn);
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private boolean isSuccess(int code) {
        return code >= HttpURLConnection.HTTP_OK
                && code < HttpURLConnection.HTTP_MULT_CHOICE;
    }

    private void verifyResCode(HttpURLConnection conn) throws IOException {
        int code = conn.getResponseCode();
        if (!isSuccess(code)) {
            throw new BadResponseCodeException("Bad response code: " + code);
        }
    }

    // TODO: 2017/7/2 process 3XX code, just: conn.setInstanceFollowRedirects(true) ?
}
