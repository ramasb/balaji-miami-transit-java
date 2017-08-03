package mdc.transit.train;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import mdc.transit.train.station.Record;
import mdc.transit.train.station.RecordSet;
import mdc.transit.train.tracker.TrainTrackerType;

public class TrainTrackerUtil {

	private final static TrainTrackerUtil INSTANCE = new TrainTrackerUtil();

	private List<Record> records;

	private TrainTrackerUtil() {
		loadStations();
	}

	public static TrainTrackerUtil getInstance() {
		return INSTANCE;
	}

	private void loadStations() {
		RecordSet recordSet = null;
		try (InputStream inputstream = this.getClass().getResourceAsStream("TrainStations.json")) {
			recordSet = RecordSet.fromJson(inputstream);
			records = recordSet.getRecordSet().getRecords();
			for (Record record : records) {
				System.out.println(record.getStation());
			}
		} catch (IOException e) {

		}
	}

	public TrainTrackerType retrieveTrainTracker(String stationId) throws Exception {
		TrainTrackerType trackerType = null;
		Client client = Client.create();
		client.setConnectTimeout(300000);
		client.setReadTimeout(300000);
		AsyncWebResource webResource = client
				.asyncResource("https://www.miamidade.gov/transit/mobile/xml/TrainTracker/?StationID=" + stationId);
		Future<ClientResponse> response = webResource.type("application/xml").get(ClientResponse.class);

		if (response.get().getStatus() == 200) {
			trackerType = response.get().getEntity(TrainTrackerType.class);
		} else {
			throw new Exception("Status : " + response.get().getStatus());
		}
		return trackerType;
	}

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}
}
