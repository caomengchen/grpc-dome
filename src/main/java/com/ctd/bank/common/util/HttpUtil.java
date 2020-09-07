package com.ctd.bank.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.ctd.bank.common.annotation.LogAspect;
import com.ctd.bank.common.bean.model.InterFaceLogModel;
import com.ctd.bank.common.dao.IBaseDAO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Component
public class HttpUtil {

	private static IBaseDAO baseDao;
	
	@Autowired
	public HttpUtil(IBaseDAO iBaseDao) {
		HttpUtil.baseDao = iBaseDao;
	}
	
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
	
	public static String httpPost_json(String url, Map map){
		System.out.println(baseDao);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse result = null;
		String returnStr = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(JSON.toJSONString(map), "utf-8");
			entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
			httpPost.setConfig(requestConfig);
			// 执行请求  
			result = httpClient.execute(httpPost);
			System.out.println(result.getStatusLine());
			returnStr = EntityUtils.toString(result.getEntity(),"utf-8");
			System.out.println("返回-->\n" + returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源  
				if (result != null) {
					result.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnStr;
    }
	
	public static String httpPost_json(String url, Map map, String name){
		String returnStr = httpPost_json(url, map);
		insertLog(url, JSON.toJSONString(map), returnStr, name, LogAspect.getLocalValue());
		return returnStr;
	}
	
	@SuppressWarnings("unchecked")
	public static void insertLog(String url, String request, String returnInfo, String name, String num){
		try {
			System.out.println("接口日志：\n接口名称：" + name + "\n请求地址：" + url + "\n业务流水号：" + num + "\n请求参数：" + request + "\n返回内容：" + returnInfo);
			InterFaceLogModel interFaceLogModel = new InterFaceLogModel();
			interFaceLogModel.setBusinessNo(num);
			interFaceLogModel.setJymc(name);
			interFaceLogModel.setParam(request);
			interFaceLogModel.setReturnInfo(returnInfo);
			interFaceLogModel.setServiceurl(url);
			baseDao.insert("tb.log.interfaceInsert", interFaceLogModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String httpPost_form(String url, Map map){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse result = null;
		String returnStr = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			for (Object key : map.keySet()) {
				values.add(new BasicNameValuePair(key.toString(),  map.get(key).toString()));
			}
            HttpEntity entity = new UrlEncodedFormEntity(values,"utf-8");
            httpPost.setEntity(entity);
			httpPost.setConfig(requestConfig);
			// 执行请求
			result = httpClient.execute(httpPost);
			System.out.println(result.getStatusLine());
			returnStr = EntityUtils.toString(result.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (result != null) {
					result.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnStr;
    }
	
	
	public static String httpPost_form(String url, Map map, String name){
		String returnStr = httpPost_form(url, map);
		insertLog(url, JSON.toJSONString(map), returnStr, name, LogAspect.getLocalValue());
		return returnStr;
	}

	public static String httpPost_formdata(String url, Map<String, Object> map){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse result = null;
		String returnStr = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
			meb.setCharset(java.nio.charset.Charset.forName("UTF-8"));
			meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (Object key : map.keySet()) {
				if(map.get(key) instanceof File){
					meb.addBinaryBody((String)key , (File) map.get(key),ContentType.MULTIPART_FORM_DATA,((new Date()).getTime())+".pdf");
				}else if(map.get(key) instanceof byte[]){
					meb.addBinaryBody((String)key , (byte[]) map.get(key),ContentType.MULTIPART_FORM_DATA,((new Date()).getTime())+".pdf");
				}else if(map.get(key) instanceof String){
					ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
					meb.addTextBody((String)key , (String)map.get(key),contentType);
				}
			}
			HttpEntity entity = meb.build();
			httpPost.setEntity(entity);
			httpPost.setConfig(requestConfig);
			// 执行请求
			result = httpClient.execute(httpPost);
			System.out.println(result.getStatusLine());
			returnStr = EntityUtils.toString(result.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (result != null) {
					result.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnStr;
	}




    public static  String  httpPostWithjson(String url, String jsonRootBean) throws IOException {
        System.out.println("jsonRootBean==="+jsonRootBean);
        CloseableHttpClient httpClient = null;
     CloseableHttpResponse result = null;
        String returnStr = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonRootBean, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setConfig(requestConfig);
            // 执行请求
            result = httpClient.execute(httpPost);
            System.out.println(result.getStatusLine());
            returnStr = EntityUtils.toString(result.getEntity(),"utf-8");
            System.out.println("返回-->\n" + returnStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (result != null) {
                    result.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnStr;




	}

	public static String uploadlzbg(String url, byte[] bytes) throws Exception {
		byte[] buf = bytes;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-type","binary/octet-stream;charset=UTF-8");
		httppost.setEntity(new ByteArrayEntity(buf));
		HttpResponse response = null;
		String conResult ="";
		try {
			response = httpclient.execute(httppost);
			if (response.getStatusLine() .getStatusCode() == 200){
				conResult = EntityUtils.toString(response.getEntity(),"UTF-8").trim();
			} else {
				throw new Exception();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return conResult;
	}

	public static String httpGet(String url){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse result = null;
		String returnStr = null;
		try {
			RequestConfig globalConfig = RequestConfig.custom()
					.setCookieSpec(CookieSpecs.DEFAULT)
					.build();
			httpClient = HttpClients.custom()
					.setDefaultRequestConfig(globalConfig)
					.build();
			RequestConfig requestConfig = RequestConfig.copy(globalConfig)
					.setCookieSpec(CookieSpecs.STANDARD)
					.build();
			System.out.println("地址-->\n" + url);
			HttpGet httpGet = new HttpGet(url);
//			httpClient = HttpClients.createDefault();
//			RequestConfig requestConfig = RequestConfig.custom()
//					.setConnectTimeout(5000).setConnectionRequestTimeout(3000)
//					.setSocketTimeout(5000).build();
			httpGet.setConfig(requestConfig);
			// 执行请求  
			result = httpClient.execute(httpGet);
			System.out.println(result.getStatusLine());
			returnStr = EntityUtils.toString(result.getEntity(),"utf-8");
			System.out.println("返回-->\n" + returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源  
				if (result != null) {
					result.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnStr;
    }

}
