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
 * <p>Java class for UploadItemsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UploadItemsType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element name="Items" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyArrayOfUploadItemsType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadItemsType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "items"
})
public class UploadItemsType
    extends BaseRequestType
{

    @XmlElement(name = "Items", required = true)
    protected NonEmptyArrayOfUploadItemsType items;

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyArrayOfUploadItemsType }
     *     
     */
    public NonEmptyArrayOfUploadItemsType getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyArrayOfUploadItemsType }
     *     
     */
    public void setItems(NonEmptyArrayOfUploadItemsType value) {
        this.items = value;
    }

}
