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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SmartResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SmartResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}SmartResponseBaseType">
 *       &lt;sequence>
 *         &lt;element name="NewBodyContent" type="{http://schemas.microsoft.com/exchange/services/2006/types}BodyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmartResponseType", propOrder = {
    "newBodyContent"
})
@XmlSeeAlso({
    ReplyAllToItemType.class,
    CancelCalendarItemType.class,
    ForwardItemType.class,
    ReplyToItemType.class
})
public class SmartResponseType
    extends SmartResponseBaseType
{

    @XmlElement(name = "NewBodyContent")
    protected BodyType newBodyContent;

    /**
     * Gets the value of the newBodyContent property.
     * 
     * @return
     *     possible object is
     *     {@link BodyType }
     *     
     */
    public BodyType getNewBodyContent() {
        return newBodyContent;
    }

    /**
     * Sets the value of the newBodyContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BodyType }
     *     
     */
    public void setNewBodyContent(BodyType value) {
        this.newBodyContent = value;
    }

}