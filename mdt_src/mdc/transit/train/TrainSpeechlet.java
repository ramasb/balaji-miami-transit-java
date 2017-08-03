/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package mdc.transit.train;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import mdc.transit.train.station.Record;
import mdc.transit.train.tracker.TrainTrackerType;

/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
public class TrainSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(TrainSpeechlet.class);

	private static final String STATION_KEY = "STATION";
	private static final String STATION_SLOT = "Station";

	private static final String MODE_KEY = "TRANSITMODE";
	private static final String MODE_SLOT = "TransitMode";

	private static final String TRAIN_MODE = "Train";
	private static final String BUS_MODE = "Bus";

	public TrainSpeechlet() {

	}

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		SpeechletResponse response = null;
		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;
		log.info("Intent : " + intentName);
		try {
			if ("GetArrivalsIntent".equals(intentName)) {
				response = getArrivalResponse(intent, session);
			} else if ("RepeatIntent".equals(intentName)) {
				response = getRepeatResponse(intent, session);
			} else if ("NextIntent".equals(intentName)) {
				response = getNextArrivalResponse(intent, session);
			} else if ("AMAZON.StopIntent".equals(intentName) || "AMAZON.CancelIntent".equals(intentName)) {
				response = getExitResponse("Thanks, goodbye.");
			} else if ("AMAZON.HelpIntent".equals(intentName)) {
				response = getHelpResponse();
			} else {
				response = getSpeechletResponse("Error", "Invalid Intent", true);
			}
		} catch (Throwable tr) {
			log.info("Intent : " + intentName + " - " + tr.getMessage());
			response = getSpeechletResponse("Error", "Error", true);
		}
		if (!"AMAZON.HelpIntent".equals(intentName)) {
			response.setShouldEndSession(true);
		}
		return response;
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Welcome to the Balaji's Miamidade Transit Helper, you can find the arrival time of trains";

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Miamidade Transit");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with arrival message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 * @throws Exception
	 */
	private SpeechletResponse getArrivalResponse(final Intent intent, final Session session) throws Exception {
		// Get the slots from the intent.
		Map<String, Slot> slots = 	intent.getSlots();
		// Get the STATION slot from the list of slots.
		Slot stationSlot = slots.get(STATION_SLOT);
		Slot modeSlot = slots.get(MODE_SLOT);
		String transitMode = TRAIN_MODE;
		SpeechletResponse response = null;

		String currentStation = null;
		String transitValue = null;
		if (modeSlot != null) {
			transitValue = modeSlot.getValue();
			if (TRAIN_MODE.equalsIgnoreCase(transitValue) || BUS_MODE.equalsIgnoreCase(transitValue)) {
				transitMode = transitValue;
			}
		}
		session.setAttribute(MODE_KEY, transitMode);
		if (stationSlot != null) {
			// Store the user's favorite STATION in the Session and create
			// response.
			currentStation = stationSlot.getValue();
			session.setAttribute(STATION_KEY, currentStation);
			response = getTrainResponseIndex(transitMode, currentStation, 1);
		} else {
			response = getUnknownStationResponse(currentStation);
		}
		return response;
	}

	protected SpeechletResponse getTrainResponse(String transitMode, String currentStation) throws Exception {

		TrainTrackerUtil util = TrainTrackerUtil.getInstance();
		SpeechletResponse response = getPlainResponse("Start");
		String speechText = "";
		String repromptText = null;
		Record record = null;
		TrainTrackerType trackerType = null;
		// Check for favorite STATION and create output to user.
		log.info("Mode : " + transitMode + "| Station : " + currentStation);
		record = retrieveRecord(currentStation);

		if (record != null) {
			log.info("Address : " + record.getAddress());
			trackerType = util.retrieveTrainTracker(record.getStationID());
			if (trackerType != null && trackerType.getInfo() != null) {
				if (trackerType.getInfo().getNBTime1Arrival() != null) {
					log.info("Tracker : " + trackerType.getInfo().getStationName());
					speechText += String.format("Next Northbound train arrival at %s station is %s in %s. ",
							trackerType.getInfo().getStationName(), trackerType.getInfo().getNBTime1Arrival(),
							convertMinHrs(trackerType.getInfo().getNBTime1()));
				}
				if (trackerType.getInfo().getSBTime1Arrival() != null) {

					speechText += String.format("Next southbound train arrival at %s stations is %s in %s. ",
							trackerType.getInfo().getStationName(), trackerType.getInfo().getSBTime1Arrival(),
							convertMinHrs(trackerType.getInfo().getSBTime1()));
				}
				if (trackerType.getInfo().getNBTime2Arrival() != null) {
					speechText += String.format("Second Northbound train arrival at %s station is %s in %s. ",
							trackerType.getInfo().getStationName(), trackerType.getInfo().getNBTime2Arrival(),
							convertMinHrs(trackerType.getInfo().getNBTime2()));
				}
				if (trackerType.getInfo().getSBTime1Arrival() != null) {
					speechText += String.format("Next southbound train arrival at %s station is %s in %s. ",
							trackerType.getInfo().getStationName(), trackerType.getInfo().getSBTime2Arrival(),
							convertMinHrs(trackerType.getInfo().getSBTime2()));
				}
				if (trackerType.getInfo().getNBTime3Arrival() != null) {
					speechText += String.format("Third Northbound train arrival at %s station is %s in %s. ",
							trackerType.getInfo().getStationName(), trackerType.getInfo().getNBTime3Arrival(),
							convertMinHrs(trackerType.getInfo().getNBTime3()));

				}
				if (trackerType.getInfo().getSBTime3Arrival() != null) {
					speechText += String.format("Next southbound train arrival at %s station is %s in %s. ",
							trackerType.getInfo().getStationName(), trackerType.getInfo().getSBTime3Arrival(),
							convertMinHrs(trackerType.getInfo().getSBTime3()));

				}
				repromptText = "You can ask me the next train arrival for your STATION by saying, when is the Train at Brickell? Or tell Stop or Cancel to exit.";
			} else {
				speechText = String.format("There is no information for this %s station, please try again",
						currentStation);
				repromptText = "You can ask me the next train arrival for your STATION by saying, when is the Train at Brickell?	";
			}
			response = getSpeechletResponse(speechText, repromptText, true);
		} else {
			response = getUnknownStationResponse(currentStation);
		}

		return response;
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with repeat arrival message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 * @throws Exception
	 */
	private SpeechletResponse getRepeatResponse(final Intent intent, final Session session) throws Exception {
		String currentStation = (String) session.getAttribute(STATION_KEY);
		String transitMode = (String) session.getAttribute(MODE_KEY);
		currentStation = currentStation == null ? "" : currentStation;
		transitMode = transitMode == null ? "" : transitMode;
		return getTrainResponseIndex(transitMode, currentStation, 1);
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with next arrival message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 * @throws Exception
	 */
	private SpeechletResponse getNextArrivalResponse(final Intent intent, final Session session) throws Exception {
		String currentStation = (String) session.getAttribute(STATION_KEY);
		String transitMode = (String) session.getAttribute(MODE_KEY);
		currentStation = currentStation == null ? "" : currentStation;
		transitMode = transitMode == null ? "" : transitMode;
		return getTrainResponseIndex(transitMode, currentStation, 2);
	}

	private SpeechletResponse getTrainResponseIndex(String transitMode, String currentStation, int index)
			throws Exception {

		TrainTrackerUtil util = TrainTrackerUtil.getInstance();
		;
		SpeechletResponse response = null;
		String speechText = "";
		String repromptText = null;
		Record record = null;
		TrainTrackerType trackerType = null;
		// Check for favorite STATION and create output to user.

		record = retrieveRecord(currentStation);
		if (record != null) {
			trackerType = util.retrieveTrainTracker(record.getStationID());
			if (trackerType != null && trackerType.getInfo() != null) {
				if (trackerType.getInfo().getNBTime1Arrival() != null) {
					if (index == 1) {
						speechText += String.format("First Northbound train arrival at %s station is %s in %s",
								trackerType.getInfo().getStationName(), trackerType.getInfo().getNBTime1Arrival(),
								convertMinHrs(trackerType.getInfo().getNBTime1()));
					} else if (index == 2) {
						speechText += String.format("Second Northbound train arrival at %s station is %s in %s",
								trackerType.getInfo().getStationName(), trackerType.getInfo().getNBTime2Arrival(),
								convertMinHrs(trackerType.getInfo().getNBTime2()));
					} else if (index == 3) {
						speechText += String.format("Third Northbound train arrival at %s station is %s in %s",
								trackerType.getInfo().getStationName(), trackerType.getInfo().getNBTime3Arrival(),
								convertMinHrs(trackerType.getInfo().getNBTime3()));
					}
				}
				if (trackerType.getInfo().getSBTime1Arrival() != null) {
					if (speechText.length() > 0) {
						speechText += " and ";
					}
					if (index == 1) {
						speechText += String.format("First southbound train arrival at %s stations is %s in %s",
								trackerType.getInfo().getStationName(), trackerType.getInfo().getSBTime1Arrival(),
								convertMinHrs(trackerType.getInfo().getSBTime1()));
					} else if (index == 2) {
						speechText += String.format("Second southbound train arrival at %s station is %s in %s",
								trackerType.getInfo().getStationName(), trackerType.getInfo().getSBTime2Arrival(),
								convertMinHrs(trackerType.getInfo().getSBTime2()));
					} else if (index == 3) {
						speechText += String.format("Third southbound train arrival at %s station is %s in %s",
								trackerType.getInfo().getStationName(), trackerType.getInfo().getSBTime3Arrival(),
								convertMinHrs(trackerType.getInfo().getSBTime3()));
					}
				}
				repromptText = "To get next train, tell next train. Or to exit, tell Stop or Cancel";
			} else {
				speechText = String.format("There is no information for this %s station, please try again",
						currentStation);
				repromptText = "You can ask me the next train arrival for your STATION by saying, when is the Train at Brickell?";
			}
			response = getSpeechletResponse(speechText, repromptText, true);
		} else {
			response = getUnknownStationResponse(currentStation);
		}
		return response;
	}

	private SpeechletResponse getUnknownStationResponse(String currentStation) {
		// Render an error since we don't know what the users
		String speechText = String.format("I'm not sure about this %s station is, please try again", currentStation);
		String repromptText = "I'm not sure about this STATION is. You ask me the next train arrival for your STATION by saying, when is the Train at Brickell?";
		return getSpeechletResponse(speechText, repromptText, true);
	}

	private String convertMinHrs(String arrivalTime) {
		String time = "";

		if (arrivalTime.contains("sec")) {
			arrivalTime = StringUtils.replace(arrivalTime, " sec", "");
			String t[] = StringUtils.split(arrivalTime, ":");
			time += t[1] + " second";
			if (Integer.parseInt(t[1]) > 1) {
				time += "s";
			}
		}
		if (arrivalTime.contains("min")) {
			arrivalTime = StringUtils.replace(arrivalTime, " min", "");
			String t[] = StringUtils.split(arrivalTime, ":");
			time += t[0] + " minute";
			if (Integer.parseInt(t[0]) > 1) {
				time += "s";
			}
			time += " and " + t[1] + " second";
			if (Integer.parseInt(t[1]) > 1) {
				time += "s";
			}
		}
		if (arrivalTime.contains("hr")) {
			arrivalTime = StringUtils.replace(arrivalTime, " hr", "");
			String t[] = StringUtils.split(arrivalTime, ":");
			time += t[0] + " hour";
			if (Integer.parseInt(t[0]) > 1) {
				time += "s";
			}
			time += " and " + t[1] + " minute";
			if (Integer.parseInt(t[1]) > 1) {
				time += "s";
			}
		}
		return time;
	}

	private Record retrieveRecord(String stationName) {
		Record record = null;
		List<Record> records = TrainTrackerUtil.getInstance().getRecords();
		if (records != null && records.size() > 0) {
			for (Record dbRecord : records) {
				if (dbRecord.getStation().equalsIgnoreCase(stationName)) {
					record = dbRecord;
					break;
				}
			}
		}
		return record;
	}

	/**
	 * Creates a {@code SpeechletResponse} for the help intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse() {
		String speechText = "You ask me the next train arrival for your STATION by saying, when is the Train at Brickell?";

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Help");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	/**
	 * Returns a Speechlet response for a speech and reprompt text.
	 */
	private SpeechletResponse getSpeechletResponse(String speechText, String repromptText, boolean isAskResponse) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Miami Transit");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		if (isAskResponse) {
			// Create reprompt
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(repromptText);
			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);

			return SpeechletResponse.newAskResponse(speech, reprompt, card);

		} else {
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}

	/**
	 * Returns a Speechlet response for a speech.
	 */
	private SpeechletResponse getExitResponse(String speechText) {
		SpeechletResponse response = getPlainResponse(speechText);
		response.setShouldEndSession(true);
		return response;
	}

	/**
	 * Returns a Speechlet response for a speech.
	 */
	private SpeechletResponse getPlainResponse(String speechText) {
		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);
		return SpeechletResponse.newTellResponse(speech);
	}
}
