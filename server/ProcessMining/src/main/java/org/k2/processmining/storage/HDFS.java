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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nyq on 2017/6/13.
 */
//@Component
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
        httpPUT(HDFS_URL + "?" + MKDIRS + "&user.name=" + OWNER);
    }

    private String getUploadUrl(AbstractLog log) {
        return getLogLocation(log) + "?" + CREATE + "&user.name=" + OWNER + "&overwrite=true";
    }

    private String getDownloadUrl(AbstractLog log) {
        return getLogLocation(log) + "?" + OPEN;
    }

    private String getDeleteUrl(AbstractLog log) {
        return getLogLocation(log) + "?" + DELETE;
    }

    @Override
    public String getLogLocation(AbstractLog log) {
        if (log == null || log.getUserId() == null || log.getId() == null) {
            LOGGER.error("Illegal input!");
            throw new BadRequestException("日志不存在！");
        }
        return HDFS_URL + "/" + log.getUserId() + "/" + log.getType() + "/" + log.getId();
    }

    @Override
    public boolean makeDirectoryForUser(User user) {
        for (String logTypeName : Util.getLogTypeNames()) {
            String reqUrl = HDFS.HDFS_URL + "/" + user.getId() + "/" + logTypeName + "?" + MKDIRS + "&user.name=" + OWNER;
            LOGGER.debug("make dir: {}", reqUrl);
            if(! httpPUT(reqUrl)) {
                LOGGER.error("fail to make dir for user: {}", reqUrl);
                deleteUser(user);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isExist(AbstractLog log) {
        return isExist(getLogLocation(log) + "?" + LISTSTATUS);
    }

    @Override
    public boolean isExist(User user) {
        for (String logTypeName : Util.getLogTypeNames()) {
            String reqUrl = HDFS.HDFS_URL + "/" + user.getId() + "/" + logTypeName + "?" + LISTSTATUS;
            if (!isExist(reqUrl)) {
                return false;
            }
        }
        return true;
    }

    private boolean isExist(String url) {
        return httpGET(url);
    }

    private boolean httpPUT(String reqUrl) {
        return httpReq(reqUrl, "PUT");
    }

    private boolean httpGET(String reqUrl) {
        return httpReq(reqUrl, "GET");
    }

    private boolean httpReq(String reqUrl, String method) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.connect();
            return isSuccess(conn.getResponseCode());
        } catch (IOException e) {
            LOGGER.error("HTTP request fail: {}:{}", method, reqUrl, e);
            throw new InternalServerErrorException();
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public boolean deleteUser(User user) {
        String reqUrl = HDFS_URL + "/" + user.getId() + "?" + DELETE + "&recursive=true";
        return delete(reqUrl);
    }

    @Override
    public boolean upload(AbstractLog log, InputStream inputStream) {
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
            return isSuccess(conn.getResponseCode());
        } catch (IOException e) {
            LOGGER.error("Fail to upload: {}:{}", log.getType(), log.getId(), e);
            throw new InternalServerErrorException(Message.INTERNAL_SERVER_ERROR);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public <T> T upload(AbstractLog log, ProcessOutputStream<T> processOutputStream) {
        String reqUrl = getUploadUrl(log);
        System.out.println("upload: "+reqUrl);
        URL url;
        HttpURLConnection conn = null;
        T res = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
//           conn.setConnectTimeout(5000);
//	         conn.setReadTimeout(30000);
            res = processOutputStream.processOutputStream(conn.getOutputStream());
            if (!isSuccess(conn.getResponseCode())) {
                res = null;
            }
        } catch (IOException e) {
            LOGGER.error("Fail to processOutputStream:", e);
            throw new InternalServerErrorException(Message.INTERNAL_SERVER_ERROR);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    @Override
    public boolean download(AbstractLog log, OutputStream outputStream) {
        String reqUrl = getDownloadUrl(log);
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.connect();
            IOUtils.copyLarge(conn.getInputStream(), outputStream);
            return isSuccess(conn.getResponseCode());
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"))) {
//                String line="";
//                while ((line = reader.readLine()) != null){
//                    writer.write(line);
//                    writer.write("\n");
//                }
//                return isSuccess(conn.getResponseCode());
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            LOGGER.error("Fail to download: {}:{}", log.getType(), log.getId(), e);
            throw new InternalServerErrorException(Message.INTERNAL_SERVER_ERROR);
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public <T> T download(AbstractLog log, ProcessInputStream<T> processInputStream) {
        String reqUrl = getDownloadUrl(log);
        System.out.println("download:" + reqUrl);
        URL url;
        HttpURLConnection conn = null;
        T res = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.connect();
            res = processInputStream.processInputStream(conn.getInputStream());
            if (! isSuccess(conn.getResponseCode())) {
                res = null;
            }
        } catch (IOException e) {
            LOGGER.error("Fail to download: {}:{}", log.getType(), log.getId(), e);
            throw new InternalServerErrorException(Message.INTERNAL_SERVER_ERROR);
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    @Override
    public boolean delete(AbstractLog log) {
        String reqUrl = getDeleteUrl(log);
        return delete(reqUrl);
    }

    private boolean delete(String reqUrl) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.connect();
            return isSuccess(conn.getResponseCode());

        } catch (IOException e) {
            LOGGER.error("HTTP delete fail: {}", reqUrl, e);
            throw new InternalServerErrorException(Message.INTERNAL_SERVER_ERROR);
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
}
