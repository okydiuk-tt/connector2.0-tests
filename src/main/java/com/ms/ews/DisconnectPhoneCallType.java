//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.07 at 08:14:05 PM EET 
//


package com.ms.ews;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DisconnectPhoneCallType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DisconnectPhoneCallType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element name="PhoneCallId" type="{http://schemas.microsoft.com/exchange/services/2006/types}PhoneCallIdType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisconnectPhoneCallType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "phoneCallId"
})
public class DisconnectPhoneCallType
    extends BaseRequestType
{

    @XmlElement(name = "PhoneCallId", required = true)
    protected PhoneCallIdType phoneCallId;

    /**
     * Gets the value of the phoneCallId property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneCallIdType }
     *     
     */
    public PhoneCallIdType getPhoneCallId() {
        return phoneCallId;
    }

    /**
     * Sets the value of the phoneCallId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneCallIdType }
     *     
     */
    public void setPhoneCallId(PhoneCallIdType value) {
        this.phoneCallId = value;
    }

}
