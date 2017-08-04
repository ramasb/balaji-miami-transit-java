package mdc.transit.train;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.async.TypeListener;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.non.blocking.NonBlockingClient;
import com.sun.jersey.client.non.blocking.config.DefaultNonBlockingClientConfig;

import mdc.transit.train.tracker.TrainTrackerType;

public class TrainTrackerUtil {
	private static final Logger log = LoggerFactory.getLogger(TrainTrackerUtil.class);
	volatile boolean completed = false;
	TrainTrackerType trackerType = null;
	private static final String STATION_URL = "https://www.miamidade.gov/transit/mobile/xml/TrainTracker/?StationID=";

	public TrainTrackerUtil() {
	}

	public TrainTrackerType retrieveTrainTracker(String stationId) throws Exception {
		TrainTrackerType trackerType = null;
		ClientConfig cc = new DefaultNonBlockingClientConfig();
		Client client = NonBlockingClient.create(cc);
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
		ClientConfig cc = new DefaultNonBlockingClientConfig();
		Client client = NonBlockingClient.create(cc);
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
