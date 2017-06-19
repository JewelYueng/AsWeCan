package org.k2.processmining.storage;


import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.log.LogType;
import org.k2.processmining.model.user.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nyq on 2017/6/13.
 */
public class HDFS implements LogStorage {
    private static final String url = "http://116.56.129.93:50070/webhdfs/v1/ProcessMining";
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
        // mkdirs..
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
        return url + "/" + log.getUserId() + "/" + log.getType() + "/" + log.getId();
    }

    @Override
    public boolean makeDirectoryForUser(User user) {
        for (LogType logType : LogType.values()) {
            String dir = logType.value;
            String reqUrl = HDFS.url + "/" + user.getId() + "/" + dir + "?" + MKDIRS + "&user.name=AsWeCan";
            System.out.println(reqUrl);
            URL url;
            HttpURLConnection conn = null;
            try {
                url = new URL(reqUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.connect();
                if(!isSuccess(conn.getResponseCode())) {
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally{
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return true;
    }

    @Override
    public boolean deleteUser(User user) {
        String reqUrl = url + "/" + user.getId() + "?" + DELETE + "&recursive=true";
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

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(conn.getOutputStream());
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)){
                byte[] content = new byte[1024];
                int byteRead = 0;
                while ((byteRead = bufferedInputStream.read(content)) != -1) {
                    bufferedOutputStream.write(content, 0, byteRead);
                }
                bufferedOutputStream.flush();
                return isSuccess(conn.getResponseCode());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    @Override
    public <T> T upload(AbstractLog log, ProcessOutputStream<T> processOutputStream) {
        String reqUrl = getUploadUrl(log);
        System.out.println("upload: "+reqUrl);
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
            T res = processOutputStream.processOutputStream(conn.getOutputStream());
            if (res != null && isSuccess(conn.getResponseCode())) {
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
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
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            try (BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream())){
                byte[] content = new byte[1024];
                int byteRead = 0;
                while ((byteRead = inputStream.read(content)) != -1) {
                    bufferedOutputStream.write(content, 0, byteRead);
                }
                bufferedOutputStream.flush();
                return isSuccess(conn.getResponseCode());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
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
            e.printStackTrace();
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    @Override
    public <T> T download(AbstractLog log, ProcessInputStream<T> processInputStream) {
        String reqUrl = getDownloadUrl(log);
        System.out.println("download:" + reqUrl);
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.connect();
            T res = processInputStream.processInputStream(conn.getInputStream());
            if (res != null && isSuccess(conn.getResponseCode())) {
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
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
            e.printStackTrace();
        }
        finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    private boolean isSuccess(int code) {
        return code >= HttpURLConnection.HTTP_OK
                && code < HttpURLConnection.HTTP_MULT_CHOICE;
    }
}
