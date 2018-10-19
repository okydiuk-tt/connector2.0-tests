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
 * <p>Java class for WorkingHours complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WorkingHours">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TimeZone" type="{http://schemas.microsoft.com/exchange/services/2006/types}SerializableTimeZone"/>
 *         &lt;element name="WorkingPeriodArray" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfWorkingPeriod"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkingHours", propOrder = {
    "timeZone",
    "workingPeriodArray"
})
public class WorkingHours {

    @XmlElement(name = "TimeZone", required = true)
    protected SerializableTimeZone timeZone;
    @XmlElement(name = "WorkingPeriodArray", required = true)
    protected ArrayOfWorkingPeriod workingPeriodArray;

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link SerializableTimeZone }
     *     
     */
    public SerializableTimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link SerializableTimeZone }
     *     
     */
    public void setTimeZone(SerializableTimeZone value) {
        this.timeZone = value;
    }

    /**
     * Gets the value of the workingPeriodArray property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWorkingPeriod }
     *     
     */
    public ArrayOfWorkingPeriod getWorkingPeriodArray() {
        return workingPeriodArray;
    }

    /**
     * Sets the value of the workingPeriodArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWorkingPeriod }
     *     
     */
    public void setWorkingPeriodArray(ArrayOfWorkingPeriod value) {
        this.workingPeriodArray = value;
    }

}