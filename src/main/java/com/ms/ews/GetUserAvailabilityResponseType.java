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
 * <p>Java class for GetUserAvailabilityResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUserAvailabilityResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FreeBusyResponseArray" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ArrayOfFreeBusyResponse" minOccurs="0"/>
 *         &lt;element name="SuggestionsResponse" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SuggestionsResponseType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUserAvailabilityResponseType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "freeBusyResponseArray",
    "suggestionsResponse"
})
public class GetUserAvailabilityResponseType {

    @XmlElement(name = "FreeBusyResponseArray")
    protected ArrayOfFreeBusyResponse freeBusyResponseArray;
    @XmlElement(name = "SuggestionsResponse")
    protected SuggestionsResponseType suggestionsResponse;

    /**
     * Gets the value of the freeBusyResponseArray property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFreeBusyResponse }
     *     
     */
    public ArrayOfFreeBusyResponse getFreeBusyResponseArray() {
        return freeBusyResponseArray;
    }

    /**
     * Sets the value of the freeBusyResponseArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFreeBusyResponse }
     *     
     */
    public void setFreeBusyResponseArray(ArrayOfFreeBusyResponse value) {
        this.freeBusyResponseArray = value;
    }

    /**
     * Gets the value of the suggestionsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link SuggestionsResponseType }
     *     
     */
    public SuggestionsResponseType getSuggestionsResponse() {
        return suggestionsResponse;
    }

    /**
     * Sets the value of the suggestionsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuggestionsResponseType }
     *     
     */
    public void setSuggestionsResponse(SuggestionsResponseType value) {
        this.suggestionsResponse = value;
    }

}
