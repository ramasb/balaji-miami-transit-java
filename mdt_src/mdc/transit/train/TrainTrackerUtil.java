package mdc.transit.train;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.async.TypeListener;

import mdc.transit.train.tracker.ObjectFactory;
import mdc.transit.train.tracker.TrainTrackerType;

public class TrainTrackerUtil {
	private static final Logger log = LoggerFactory.getLogger(TrainTrackerUtil.class);
	volatile boolean completed = false;
	TrainTrackerType trackerType = null;
	private static final String STATION_URL = "https://www.miamidade.gov/transit/mobile/xml/TrainTracker/?StationID=";

	protected static final Integer TIMEOUT_MILLISECS = 30000;

	public TrainTrackerUtil() {
	}

	public TrainTrackerType retrieveTrainTracker(String stationId) throws Exception {
		TrainTrackerType trackerType = null;
		Random rnd = new Random();
		String url = STATION_URL + stationId + "&rnd=" + rnd.nextInt();
		CloseableHttpResponse httpResponse = null;
		CloseableHttpClient httpClient = null;
		HttpGet httpget = null;
		String xmlData = "";

		try {
			httpClient = buildClient();
			httpget = new HttpGet(url);
			httpResponse = httpClient.execute(httpget);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				xmlData = fromStream(httpResponse.getEntity().getContent());
				trackerType = getConvertedObject(xmlData);
			}
		} catch (Exception ex) {
			log.info("Exception : " + ex.toString());
		}
		return trackerType;
	}

	protected String fromStream(InputStream in) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, "UTF-8");
		return writer.toString();
	}

	protected TrainTrackerType getConvertedObject(String xmlData) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(TrainTrackerType.class, ObjectFactory.class);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StreamSource json = new StreamSource(new StringReader(xmlData));
		JAXBElement<TrainTrackerType> unmarshalledObject = unmarshaller.unmarshal(json, TrainTrackerType.class);

		TrainTrackerType tResponse = unmarshalledObject.getValue();
		return tResponse;
	}

	private CloseableHttpClient buildClient() throws UnknownHostException {

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_MILLISECS)
				.setConnectTimeout(TIMEOUT_MILLISECS).setConnectionRequestTimeout(TIMEOUT_MILLISECS).build();

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder = httpClientBuilder.setDefaultRequestConfig(requestConfig);
		setTrustManager(httpClientBuilder);

		CloseableHttpClient httpClient = httpClientBuilder.build();

		return httpClient;
	}

	public void setTrustManager(HttpClientBuilder httpClientBuilder) {
		SSLContext sslContext = null;

		try {
			if (true) {
				sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] arg0, String arg1) {
						return true;
					}
				}).build();
				httpClientBuilder.setSslcontext(sslContext);

				// or SSLConnectionSocketFactory.getDefaultHostnameVerifier(),
				// if you
				// don't want to weaken
				X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}

					public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
					}

					public void verify(String host, X509Certificate cert) throws SSLException {
					}

					@Override
					public void verify(String arg0, SSLSocket arg1) throws IOException {
						// TODO Auto-generated method stub

					}
				};

				SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
						hostnameVerifier);
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
						.<ConnectionSocketFactory>create()
						.register("http", PlainConnectionSocketFactory.getSocketFactory())
						.register("https", sslSocketFactory).build();

				// allows multi-threaded use
				PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(
						socketFactoryRegistry);
				httpClientBuilder.setConnectionManager(connMgr);
			}
		} catch (Exception e) {
		}

	}

	public TrainTrackerType retrieveTrainTracker1(String stationId) throws Exception {
		TrainTrackerType trackerType = null;
		Client client = Client.create();	
		client.setConnectTimeout(1000000);
		client.setReadTimeout(1000000);
		Random rnd = new Random();
		String url = STATION_URL + stationId + "&rnd=" + rnd.nextInt();

		AsyncWebResource webResource = client.asyncResource(url);
		Future<ClientResponse> response = webResource.type("application/xml").get(ClientResponse.class);
		if (response.get().getClientResponseStatus() == Status.OK && response.get().hasEntity()) {
			trackerType = response.get().getEntity(TrainTrackerType.class);
		} else {
			log.info("Status : " + response.get().getStatus());
		}
		return trackerType;
	}

	public TrainTrackerType retrieveAsyTrainTracker(String stationId) throws Exception {
			
		Client client = Client.create();
		client.setConnectTimeout(300000);
		client.setReadTimeout(300000);
		Random rnd = new Random();
		String url = STATION_URL + stationId + "&rnd=" + rnd.nextInt();
		log.info("Start URL : " + url);
		AsyncWebResource webResource = client.asyncResource(url);
		// Future<ClientResponse> response = webResource.get(ClientResponse.class);
		new Thread(() -> {
			try {
				webResource.get(new TypeListener<ClientResponse>(ClientResponse.class) {
					@Override
					public void onComplete(Future<ClientResponse> response) throws InterruptedException {
						try {
							if (response != null && response.get() != null) {
								log.info("Response Status : " + response.get().getStatus());
								if (response.get().getClientResponseStatus() == Status.OK
										&& response.get().hasEntity()) {
									trackerType = response.get().getEntity(TrainTrackerType.class);
								} else {
									log.info("Status : " + response.get().getStatus());
								}
							}
						} catch (ExecutionException e) {
							log.info("Execution Exception : " + e.toString());
						}
						completed = true;
					}
				});
			} catch (Exception ex) {
				log.info("Exception : " + ex.toString());
			}
		}).run();

		while (!completed) {

		}
		client.destroy();
		return trackerType;
	}
}
