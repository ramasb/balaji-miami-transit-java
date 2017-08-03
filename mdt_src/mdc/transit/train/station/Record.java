
package mdc.transit.train.station;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "StationID", "StationIDshow", "Station", "SB_OrderNum", "NB_OrderNum", "Address", "City", "State",
		"Zip", "Parking", "ConnectingOther", "PlacesOfInterest", "Other", "Airport", "TriRail", "LongTermParking",
		"Latitude", "Longitude", "svLatitude", "svLongitude", "svHeading" })
public class Record implements Serializable {

	@JsonProperty("StationID")
	private String stationID;
	@JsonProperty("StationIDshow")
	private String stationIDshow;
	@JsonProperty("Station")
	private String station;
	@JsonProperty("SB_OrderNum")
	private String sBOrderNum;
	@JsonProperty("NB_OrderNum")
	private String nBOrderNum;
	@JsonProperty("Address")
	private String address;
	@JsonProperty("City")
	private String city;
	@JsonProperty("State")
	private String state;
	@JsonProperty("Zip")
	private String zip;
	@JsonProperty("Parking")
	private String parking;
	@JsonProperty("ConnectingOther")
	private Object connectingOther;
	@JsonProperty("PlacesOfInterest")
	private Object placesOfInterest;
	@JsonProperty("Other")
	private Object other;
	@JsonProperty("Airport")
	private String airport;
	@JsonProperty("TriRail")
	private String triRail;
	@JsonProperty("LongTermParking")
	private String longTermParking;
	@JsonProperty("Latitude")
	private String latitude;
	@JsonProperty("Longitude")
	private String longitude;
	@JsonProperty("svLatitude")
	private String svLatitude;
	@JsonProperty("svLongitude")
	private String svLongitude;
	@JsonProperty("svHeading")
	private String svHeading;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = -3246685257222656902L;

	@JsonProperty("StationID")
	public String getStationID() {
		return stationID;
	}

	@JsonProperty("StationID")
	public void setStationID(String stationID) {
		this.stationID = stationID;
	}

	public Record withStationID(String stationID) {
		this.stationID = stationID;
		return this;
	}

	@JsonProperty("StationIDshow")
	public String getStationIDshow() {
		return stationIDshow;
	}

	@JsonProperty("StationIDshow")
	public void setStationIDshow(String stationIDshow) {
		this.stationIDshow = stationIDshow;
	}

	public Record withStationIDshow(String stationIDshow) {
		this.stationIDshow = stationIDshow;
		return this;
	}

	@JsonProperty("Station")
	public String getStation() {
		return station;
	}

	@JsonProperty("Station")
	public void setStation(String station) {
		this.station = station;
	}

	public Record withStation(String station) {
		this.station = station;
		return this;
	}

	@JsonProperty("SB_OrderNum")
	public String getSBOrderNum() {
		return sBOrderNum;
	}

	@JsonProperty("SB_OrderNum")
	public void setSBOrderNum(String sBOrderNum) {
		this.sBOrderNum = sBOrderNum;
	}

	public Record withSBOrderNum(String sBOrderNum) {
		this.sBOrderNum = sBOrderNum;
		return this;
	}

	@JsonProperty("NB_OrderNum")
	public String getNBOrderNum() {
		return nBOrderNum;
	}

	@JsonProperty("NB_OrderNum")
	public void setNBOrderNum(String nBOrderNum) {
		this.nBOrderNum = nBOrderNum;
	}

	public Record withNBOrderNum(String nBOrderNum) {
		this.nBOrderNum = nBOrderNum;
		return this;
	}

	@JsonProperty("Address")
	public String getAddress() {
		return address;
	}

	@JsonProperty("Address")
	public void setAddress(String address) {
		this.address = address;
	}

	public Record withAddress(String address) {
		this.address = address;
		return this;
	}

	@JsonProperty("City")
	public String getCity() {
		return city;
	}

	@JsonProperty("City")
	public void setCity(String city) {
		this.city = city;
	}

	public Record withCity(String city) {
		this.city = city;
		return this;
	}

	@JsonProperty("State")
	public String getState() {
		return state;
	}

	@JsonProperty("State")
	public void setState(String state) {
		this.state = state;
	}

	public Record withState(String state) {
		this.state = state;
		return this;
	}

	@JsonProperty("Zip")
	public String getZip() {
		return zip;
	}

	@JsonProperty("Zip")
	public void setZip(String zip) {
		this.zip = zip;
	}

	public Record withZip(String zip) {
		this.zip = zip;
		return this;
	}

	@JsonProperty("Parking")
	public String getParking() {
		return parking;
	}

	@JsonProperty("Parking")
	public void setParking(String parking) {
		this.parking = parking;
	}

	public Record withParking(String parking) {
		this.parking = parking;
		return this;
	}

	@JsonProperty("ConnectingOther")
	public Object getConnectingOther() {
		return connectingOther;
	}

	@JsonProperty("ConnectingOther")
	public void setConnectingOther(Object connectingOther) {
		this.connectingOther = connectingOther;
	}

	public Record withConnectingOther(Object connectingOther) {
		this.connectingOther = connectingOther;
		return this;
	}

	@JsonProperty("PlacesOfInterest")
	public Object getPlacesOfInterest() {
		return placesOfInterest;
	}

	@JsonProperty("PlacesOfInterest")
	public void setPlacesOfInterest(Object placesOfInterest) {
		this.placesOfInterest = placesOfInterest;
	}

	public Record withPlacesOfInterest(Object placesOfInterest) {
		this.placesOfInterest = placesOfInterest;
		return this;
	}

	@JsonProperty("Other")
	public Object getOther() {
		return other;
	}

	@JsonProperty("Other")
	public void setOther(Object other) {
		this.other = other;
	}

	public Record withOther(Object other) {
		this.other = other;
		return this;
	}

	@JsonProperty("Airport")
	public String getAirport() {
		return airport;
	}

	@JsonProperty("Airport")
	public void setAirport(String airport) {
		this.airport = airport;
	}

	public Record withAirport(String airport) {
		this.airport = airport;
		return this;
	}

	@JsonProperty("TriRail")
	public String getTriRail() {
		return triRail;
	}

	@JsonProperty("TriRail")
	public void setTriRail(String triRail) {
		this.triRail = triRail;
	}

	public Record withTriRail(String triRail) {
		this.triRail = triRail;
		return this;
	}

	@JsonProperty("LongTermParking")
	public String getLongTermParking() {
		return longTermParking;
	}

	@JsonProperty("LongTermParking")
	public void setLongTermParking(String longTermParking) {
		this.longTermParking = longTermParking;
	}

	public Record withLongTermParking(String longTermParking) {
		this.longTermParking = longTermParking;
		return this;
	}

	@JsonProperty("Latitude")
	public String getLatitude() {
		return latitude;
	}

	@JsonProperty("Latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Record withLatitude(String latitude) {
		this.latitude = latitude;
		return this;
	}

	@JsonProperty("Longitude")
	public String getLongitude() {
		return longitude;
	}

	@JsonProperty("Longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Record withLongitude(String longitude) {
		this.longitude = longitude;
		return this;
	}

	@JsonProperty("svLatitude")
	public String getSvLatitude() {
		return svLatitude;
	}

	@JsonProperty("svLatitude")
	public void setSvLatitude(String svLatitude) {
		this.svLatitude = svLatitude;
	}

	public Record withSvLatitude(String svLatitude) {
		this.svLatitude = svLatitude;
		return this;
	}

	@JsonProperty("svLongitude")
	public String getSvLongitude() {
		return svLongitude;
	}

	@JsonProperty("svLongitude")
	public void setSvLongitude(String svLongitude) {
		this.svLongitude = svLongitude;
	}

	public Record withSvLongitude(String svLongitude) {
		this.svLongitude = svLongitude;
		return this;
	}

	@JsonProperty("svHeading")
	public String getSvHeading() {
		return svHeading;
	}

	@JsonProperty("svHeading")
	public void setSvHeading(String svHeading) {
		this.svHeading = svHeading;
	}

	public Record withSvHeading(String svHeading) {
		this.svHeading = svHeading;
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

	public Record withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}