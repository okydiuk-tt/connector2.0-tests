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
 * <p>Java class for UserOofSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserOofSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OofState" type="{http://schemas.microsoft.com/exchange/services/2006/types}OofState"/>
 *         &lt;element name="ExternalAudience" type="{http://schemas.microsoft.com/exchange/services/2006/types}ExternalAudience"/>
 *         &lt;element name="Duration" type="{http://schemas.microsoft.com/exchange/services/2006/types}Duration" minOccurs="0"/>
 *         &lt;element name="InternalReply" type="{http://schemas.microsoft.com/exchange/services/2006/types}ReplyBody" minOccurs="0"/>
 *         &lt;element name="ExternalReply" type="{http://schemas.microsoft.com/exchange/services/2006/types}ReplyBody" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserOofSettings", propOrder = {
    "oofState",
    "externalAudience",
    "duration",
    "internalReply",
    "externalReply"
})
public class UserOofSettings {

    @XmlElement(name = "OofState", required = true)
    @XmlSchemaType(name = "string")
    protected OofState oofState;
    @XmlElement(name = "ExternalAudience", required = true)
    @XmlSchemaType(name = "string")
    protected ExternalAudience externalAudience;
    @XmlElement(name = "Duration")
    protected Duration duration;
    @XmlElement(name = "InternalReply")
    protected ReplyBody internalReply;
    @XmlElement(name = "ExternalReply")
    protected ReplyBody externalReply;

    /**
     * Gets the value of the oofState property.
     * 
     * @return
     *     possible object is
     *     {@link OofState }
     *     
     */
    public OofState getOofState() {
        return oofState;
    }

    /**
     * Sets the value of the oofState property.
     * 
     * @param value
     *     allowed object is
     *     {@link OofState }
     *     
     */
    public void setOofState(OofState value) {
        this.oofState = value;
    }

    /**
     * Gets the value of the externalAudience property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalAudience }
     *     
     */
    public ExternalAudience getExternalAudience() {
        return externalAudience;
    }

    /**
     * Sets the value of the externalAudience property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalAudience }
     *     
     */
    public void setExternalAudience(ExternalAudience value) {
        this.externalAudience = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setDuration(Duration value) {
        this.duration = value;
    }

    /**
     * Gets the value of the internalReply property.
     * 
     * @return
     *     possible object is
     *     {@link ReplyBody }
     *     
     */
    public ReplyBody getInternalReply() {
        return internalReply;
    }

    /**
     * Sets the value of the internalReply property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReplyBody }
     *     
     */
    public void setInternalReply(ReplyBody value) {
        this.internalReply = value;
    }

    /**
     * Gets the value of the externalReply property.
     * 
     * @return
     *     possible object is
     *     {@link ReplyBody }
     *     
     */
    public ReplyBody getExternalReply() {
        return externalReply;
    }

    /**
     * Sets the value of the externalReply property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReplyBody }
     *     
     */
    public void setExternalReply(ReplyBody value) {
        this.externalReply = value;
    }

}