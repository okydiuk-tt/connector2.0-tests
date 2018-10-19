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
 * <p>Java class for UpdateDelegateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateDelegateType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseDelegateType">
 *       &lt;sequence>
 *         &lt;element name="DelegateUsers" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfDelegateUserType" minOccurs="0"/>
 *         &lt;element name="DeliverMeetingRequests" type="{http://schemas.microsoft.com/exchange/services/2006/types}DeliverMeetingRequestsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateDelegateType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "delegateUsers",
    "deliverMeetingRequests"
})
public class UpdateDelegateType
    extends BaseDelegateType
{

    @XmlElement(name = "DelegateUsers")
    protected ArrayOfDelegateUserType delegateUsers;
    @XmlElement(name = "DeliverMeetingRequests")
    @XmlSchemaType(name = "string")
    protected DeliverMeetingRequestsType deliverMeetingRequests;

    /**
     * Gets the value of the delegateUsers property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDelegateUserType }
     *     
     */
    public ArrayOfDelegateUserType getDelegateUsers() {
        return delegateUsers;
    }

    /**
     * Sets the value of the delegateUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDelegateUserType }
     *     
     */
    public void setDelegateUsers(ArrayOfDelegateUserType value) {
        this.delegateUsers = value;
    }

    /**
     * Gets the value of the deliverMeetingRequests property.
     * 
     * @return
     *     possible object is
     *     {@link DeliverMeetingRequestsType }
     *     
     */
    public DeliverMeetingRequestsType getDeliverMeetingRequests() {
        return deliverMeetingRequests;
    }

    /**
     * Sets the value of the deliverMeetingRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliverMeetingRequestsType }
     *     
     */
    public void setDeliverMeetingRequests(DeliverMeetingRequestsType value) {
        this.deliverMeetingRequests = value;
    }

}
