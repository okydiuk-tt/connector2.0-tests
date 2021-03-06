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
 * <p>Java class for AvailabilityProxyRequestType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AvailabilityProxyRequestType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CrossSite"/>
 *     &lt;enumeration value="CrossForest"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AvailabilityProxyRequestType")
@XmlEnum
public enum AvailabilityProxyRequestType {

    @XmlEnumValue("CrossSite")
    CROSS_SITE("CrossSite"),
    @XmlEnumValue("CrossForest")
    CROSS_FOREST("CrossForest");
    private final String value;

    AvailabilityProxyRequestType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AvailabilityProxyRequestType fromValue(String v) {
        for (AvailabilityProxyRequestType c: AvailabilityProxyRequestType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
