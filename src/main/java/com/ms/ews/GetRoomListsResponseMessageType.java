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
 * <p>Java class for GetRoomListsResponseMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetRoomListsResponseMessageType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType">
 *       &lt;sequence>
 *         &lt;element name="RoomLists" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfEmailAddressesType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetRoomListsResponseMessageType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "roomLists"
})
public class GetRoomListsResponseMessageType
    extends ResponseMessageType
{

    @XmlElement(name = "RoomLists")
    protected ArrayOfEmailAddressesType roomLists;

    /**
     * Gets the value of the roomLists property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEmailAddressesType }
     *     
     */
    public ArrayOfEmailAddressesType getRoomLists() {
        return roomLists;
    }

    /**
     * Sets the value of the roomLists property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEmailAddressesType }
     *     
     */
    public void setRoomLists(ArrayOfEmailAddressesType value) {
        this.roomLists = value;
    }

}