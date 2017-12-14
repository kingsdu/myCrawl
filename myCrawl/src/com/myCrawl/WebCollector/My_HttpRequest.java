package com.myCrawl.WebCollector;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;


public class My_HttpRequest {
	
	protected String method = "GET";
	protected byte[] outputData=null;
	
	
	public My_HttpResponse response(String urls) throws Exception {
        URL url = new URL(urls);
        My_HttpResponse response = new My_HttpResponse(url);
        int code = -1;
        int maxRedirect = 100;
        HttpURLConnection con = null;
        InputStream is = null;
        try {

            for (int redirect = 0; redirect <= maxRedirect; redirect++) {
                
                if(outputData!=null){
                    OutputStream os=con.getOutputStream();
                    os.write(outputData);
                    os.close();
                }
                /*只记录第一次返回的code*/
                if (redirect == 0) {
                    response.code(code);
                }
                
                if(code==HttpURLConnection.HTTP_NOT_FOUND){
                    response.setNotFound(true);
                    return response;
                }

                boolean needBreak = false;
                switch (code) {                      
                    case HttpURLConnection.HTTP_MOVED_PERM:
                    case HttpURLConnection.HTTP_MOVED_TEMP:
                        response.setRedirect(true);
                        String location = con.getHeaderField("Location");
                        if (location == null) {
                            throw new Exception("redirect with no location");
                        }
                        String originUrl = url.toString();
                        url = new URL(url, location);
                        response.setRealUrl(url);
                        continue;
                    default:
                        needBreak = true;
                        break;
                }
                if (needBreak) {
                    break;
                }

            }

            is = con.getInputStream();
            String contentEncoding = con.getContentEncoding();
            if (contentEncoding != null && contentEncoding.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            byte[] buf = new byte[2048];
            int read;
            int sum = 0;
            int maxsize = 10000;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((read = is.read(buf)) != -1) {
                if (maxsize > 0) {
                    sum = sum + read;

                    if (maxsize > 0 && sum > maxsize) {
                        read = maxsize - (sum - read);
                        bos.write(buf, 0, read);
                        break;
                    }
                }
                bos.write(buf, 0, read);
            }

            response.content(bos.toByteArray());
            response.headers(con.getHeaderFields());
            bos.close();

            return response;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
	
}
