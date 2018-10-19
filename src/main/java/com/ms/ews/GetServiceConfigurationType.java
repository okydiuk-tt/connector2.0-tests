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
 * <p>Java class for GetServiceConfigurationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetServiceConfigurationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element name="ActingAs" type="{http://schemas.microsoft.com/exchange/services/2006/types}EmailAddressType" minOccurs="0"/>
 *         &lt;element name="RequestedConfiguration" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ArrayOfServiceConfigurationType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetServiceConfigurationType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "actingAs",
    "requestedConfiguration"
})
public class GetServiceConfigurationType
    extends BaseRequestType
{

    @XmlElement(name = "ActingAs")
    protected EmailAddressType actingAs;
    @XmlElement(name = "RequestedConfiguration", required = true)
    protected ArrayOfServiceConfigurationType requestedConfiguration;

    /**
     * Gets the value of the actingAs property.
     * 
     * @return
     *     possible object is
     *     {@link EmailAddressType }
     *     
     */
    public EmailAddressType getActingAs() {
        return actingAs;
    }

    /**
     * Sets the value of the actingAs property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailAddressType }
     *     
     */
    public void setActingAs(EmailAddressType value) {
        this.actingAs = value;
    }

    /**
     * Gets the value of the requestedConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfServiceConfigurationType }
     *     
     */
    public ArrayOfServiceConfigurationType getRequestedConfiguration() {
        return requestedConfiguration;
    }

    /**
     * Sets the value of the requestedConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfServiceConfigurationType }
     *     
     */
    public void setRequestedConfiguration(ArrayOfServiceConfigurationType value) {
        this.requestedConfiguration = value;
    }

}