
package mdc.transit.train.station;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazon.speech.json.SpeechletRequestModule;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "RecordSet" })
public class RecordSet implements Serializable {
	/**
	 * A Jackson {@code ObjectMapper} configured for our deserialization use
	 * case.
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		/*
		 * This flag is set to support forward compatibility. We can safely add
		 * new fields to request objects, they will just be ignored.
		 */
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		OBJECT_MAPPER.registerModule(new SpeechletRequestModule());
	}

	@JsonProperty("RecordSet")
	private RecordSet recordSet;

	@JsonProperty("Record")
	private List<Record> records;

	@JsonProperty("Record")
	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	@JsonIgnore

	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 3313428631487119172L;

	@JsonProperty("RecordSet")
	public RecordSet getRecordSet() {
		return recordSet;
	}

	@JsonProperty("RecordSet")
	public void setRecordSet(RecordSet recordSet) {
		this.recordSet = recordSet;
	}

	public RecordSet withRecordSet(RecordSet recordSet) {
		this.recordSet = recordSet;
		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public RecordSet withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

	/**
	 * Read a {@code RecordSet} from an {@code InputStream}. The byte stream
	 * must be UTF-8 encoded.
	 *
	 * @param in
	 *            the input stream to read from
	 * @return the record set read from the stream
	 * @throws IOException
	 *             if deserialization fails
	 */
	public static RecordSet fromJson(final InputStream in) throws IOException {
		return OBJECT_MAPPER.readValue(in, RecordSet.class);
	}

	/**
	 * Read a {@code RecordSet} from a byte array. The byte array must be UTF-8
	 * encoded.
	 *
	 * @param json
	 *            the bytes to read from.
	 * @return the record set read from the bytes.
	 * @throws IOException
	 *             if deserialization fails.
	 */
	public static RecordSet fromJson(final byte[] json) throws IOException {
		return OBJECT_MAPPER.readValue(json, RecordSet.class);
	}

	/**
	 * Read a {@code RecordSet} from a {@code String}.
	 *
	 * @param json
	 *            the String to read from
	 * @return the record set read from the String
	 * @throws IOException
	 *             if deserialization fails
	 */
	public static RecordSet fromJson(final String json) throws IOException {
		return OBJECT_MAPPER.readValue(json, RecordSet.class);
	}

}