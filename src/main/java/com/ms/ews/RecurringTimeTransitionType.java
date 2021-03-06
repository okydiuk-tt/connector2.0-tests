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
import javax.xml.datatype.Duration;


/**
 * <p>Java class for RecurringTimeTransitionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RecurringTimeTransitionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}TransitionType">
 *       &lt;sequence>
 *         &lt;element name="TimeOffset" type="{http://www.w3.org/2001/XMLSchema}duration"/>
 *         &lt;element name="Month" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecurringTimeTransitionType", propOrder = {
    "timeOffset",
    "month"
})
@XmlSeeAlso({
    RecurringDayTransitionType.class,
    RecurringDateTransitionType.class
})
public abstract class RecurringTimeTransitionType
    extends TransitionType
{

    @XmlElement(name = "TimeOffset", required = true)
    protected Duration timeOffset;
    @XmlElement(name = "Month")
    protected int month;

    /**
     * Gets the value of the timeOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getTimeOffset() {
        return timeOffset;
    }

    /**
     * Sets the value of the timeOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setTimeOffset(Duration value) {
        this.timeOffset = value;
    }

    /**
     * Gets the value of the month property.
     * 
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     */
    public void setMonth(int value) {
        this.month = value;
    }

}
