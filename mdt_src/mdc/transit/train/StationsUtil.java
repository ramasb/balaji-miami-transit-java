package mdc.transit.train;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mdc.transit.train.station.Record;
import mdc.transit.train.station.RecordSet;

public class StationsUtil {

	private final static StationsUtil INSTANCE = new StationsUtil();

	private static final Logger log = LoggerFactory.getLogger(StationsUtil.class);

	private List<Record> records;

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	private StationsUtil() {
		loadStations();
	}

	public static StationsUtil getInstance() {
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
			log.info(e.toString());
		}
	}

}
