//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11-b140731.1112 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.23 at 12:48:57 PM EDT 
//


package mdc.transit.train.tracker;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mdc.transit.train.tracker package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TrainTracker_QNAME = new QName("", "TrainTracker");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mdc.transit.train.tracker
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TrainTrackerType }
     * 
     */
    public TrainTrackerType createTrainTrackerType() {
        return new TrainTrackerType();
    }

    /**
     * Create an instance of {@link InfoType }
     * 
     */
    public InfoType createInfoType() {
        return new InfoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrainTrackerType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TrainTracker")
    public JAXBElement<TrainTrackerType> createTrainTracker(TrainTrackerType value) {
        return new JAXBElement<TrainTrackerType>(_TrainTracker_QNAME, TrainTrackerType.class, null, value);
    }

}
