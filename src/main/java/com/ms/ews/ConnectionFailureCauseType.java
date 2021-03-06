//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.07 at 08:14:05 PM EET 
//


package com.ms.ews;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConnectionFailureCauseType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ConnectionFailureCauseType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="UserBusy"/>
 *     &lt;enumeration value="NoAnswer"/>
 *     &lt;enumeration value="Unavailable"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ConnectionFailureCauseType")
@XmlEnum
public enum ConnectionFailureCauseType {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("UserBusy")
    USER_BUSY("UserBusy"),
    @XmlEnumValue("NoAnswer")
    NO_ANSWER("NoAnswer"),
    @XmlEnumValue("Unavailable")
    UNAVAILABLE("Unavailable"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    ConnectionFailureCauseType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConnectionFailureCauseType fromValue(String v) {
        for (ConnectionFailureCauseType c: ConnectionFailureCauseType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
