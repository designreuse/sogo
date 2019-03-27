package com.yihexinda.core.utils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class HttpClinetPostUtil {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpClinetPostUtil.class);

	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static SSLConnectionSocketFactory sslsf = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static SSLContextBuilder builder = null;
	private static RequestConfig defaultRequestConfig = null;

	static {
		try {
			builder = new SSLContextBuilder();
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			sslsf = new SSLConnectionSocketFactory(builder.build(),
					new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register(HTTP, new PlainConnectionSocketFactory()).register(HTTPS, sslsf).build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(500);// max connection
			cm.setDefaultMaxPerRoute(100);

			defaultRequestConfig = RequestConfig.custom()
					.setSocketTimeout(60000)
					.setConnectTimeout(60000)
					.setConnectionRequestTimeout(60000)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  初始化
	 * @return
	 * @throws Exception
	 */
	public static CloseableHttpClient getHttpClient() throws Exception {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslsf)
				.setConnectionManager(cm)
				.setConnectionManagerShared(true)
				//CloseableHttpClient的超时时间
				.setDefaultRequestConfig(defaultRequestConfig)
				.build();
		return httpClient;
	}
	/**
	 * @param url 请求url
	 * @param param 请求参数 form提交适用
	 * @return
	 */
	public static int post(String url, List<NameValuePair> param) {
		int status = -1;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param, Consts.UTF_8);
			httpPost.setEntity(urlEncodedFormEntity);
			log.info("创建httpPost:"+httpPost);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			log.info("返回httpResponse:"+httpResponse);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return status;
			}
		} catch (Exception e) {
			log.info("Exception:"+e);
			return status;
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
					log.info("关闭资源");
				}
			} catch (IOException e) {
				log.info("关闭资源出现异常"+e);
				e.printStackTrace();
			}
		}
		return 0;
	}

}
