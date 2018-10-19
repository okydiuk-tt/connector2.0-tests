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
 * <p>Java class for EmailAddressKeyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EmailAddressKeyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EmailAddress1"/>
 *     &lt;enumeration value="EmailAddress2"/>
 *     &lt;enumeration value="EmailAddress3"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EmailAddressKeyType")
@XmlEnum
public enum EmailAddressKeyType {

    @XmlEnumValue("EmailAddress1")
    EMAIL_ADDRESS_1("EmailAddress1"),
    @XmlEnumValue("EmailAddress2")
    EMAIL_ADDRESS_2("EmailAddress2"),
    @XmlEnumValue("EmailAddress3")
    EMAIL_ADDRESS_3("EmailAddress3");
    private final String value;

    EmailAddressKeyType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EmailAddressKeyType fromValue(String v) {
        for (EmailAddressKeyType c: EmailAddressKeyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
