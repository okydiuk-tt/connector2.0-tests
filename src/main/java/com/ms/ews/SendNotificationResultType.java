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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SendNotificationResultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendNotificationResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubscriptionStatus" type="{http://schemas.microsoft.com/exchange/services/2006/types}SubscriptionStatusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendNotificationResultType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "subscriptionStatus"
})
public class SendNotificationResultType {

    @XmlElement(name = "SubscriptionStatus", required = true)
    @XmlSchemaType(name = "string")
    protected SubscriptionStatusType subscriptionStatus;

    /**
     * Gets the value of the subscriptionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link SubscriptionStatusType }
     *     
     */
    public SubscriptionStatusType getSubscriptionStatus() {
        return subscriptionStatus;
    }

    /**
     * Sets the value of the subscriptionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscriptionStatusType }
     *     
     */
    public void setSubscriptionStatus(SubscriptionStatusType value) {
        this.subscriptionStatus = value;
    }

}
