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
 * <p>Java class for GetMessageTrackingReportRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetMessageTrackingReportRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;all>
 *         &lt;element name="Scope" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyStringType"/>
 *         &lt;element name="ReportTemplate" type="{http://schemas.microsoft.com/exchange/services/2006/types}MessageTrackingReportTemplateType"/>
 *         &lt;element name="RecipientFilter" type="{http://schemas.microsoft.com/exchange/services/2006/types}EmailAddressType" minOccurs="0"/>
 *         &lt;element name="MessageTrackingReportId" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyStringType"/>
 *         &lt;element name="ReturnQueueEvents" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DiagnosticsLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Properties" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfTrackingPropertiesType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetMessageTrackingReportRequestType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "scope",
    "reportTemplate",
    "recipientFilter",
    "messageTrackingReportId",
    "returnQueueEvents",
    "diagnosticsLevel",
    "properties"
})
public class GetMessageTrackingReportRequestType
    extends BaseRequestType
{

    @XmlElement(name = "Scope", required = true)
    protected String scope;
    @XmlElement(name = "ReportTemplate", required = true)
    @XmlSchemaType(name = "string")
    protected MessageTrackingReportTemplateType reportTemplate;
    @XmlElement(name = "RecipientFilter")
    protected EmailAddressType recipientFilter;
    @XmlElement(name = "MessageTrackingReportId", required = true)
    protected String messageTrackingReportId;
    @XmlElement(name = "ReturnQueueEvents")
    protected Boolean returnQueueEvents;
    @XmlElement(name = "DiagnosticsLevel")
    protected String diagnosticsLevel;
    @XmlElement(name = "Properties")
    protected ArrayOfTrackingPropertiesType properties;

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

    /**
     * Gets the value of the reportTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link MessageTrackingReportTemplateType }
     *     
     */
    public MessageTrackingReportTemplateType getReportTemplate() {
        return reportTemplate;
    }

    /**
     * Sets the value of the reportTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageTrackingReportTemplateType }
     *     
     */
    public void setReportTemplate(MessageTrackingReportTemplateType value) {
        this.reportTemplate = value;
    }

    /**
     * Gets the value of the recipientFilter property.
     * 
     * @return
     *     possible object is
     *     {@link EmailAddressType }
     *     
     */
    public EmailAddressType getRecipientFilter() {
        return recipientFilter;
    }

    /**
     * Sets the value of the recipientFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailAddressType }
     *     
     */
    public void setRecipientFilter(EmailAddressType value) {
        this.recipientFilter = value;
    }

    /**
     * Gets the value of the messageTrackingReportId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageTrackingReportId() {
        return messageTrackingReportId;
    }

    /**
     * Sets the value of the messageTrackingReportId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageTrackingReportId(String value) {
        this.messageTrackingReportId = value;
    }

    /**
     * Gets the value of the returnQueueEvents property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReturnQueueEvents() {
        return returnQueueEvents;
    }

    /**
     * Sets the value of the returnQueueEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturnQueueEvents(Boolean value) {
        this.returnQueueEvents = value;
    }

    /**
     * Gets the value of the diagnosticsLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosticsLevel() {
        return diagnosticsLevel;
    }

    /**
     * Sets the value of the diagnosticsLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosticsLevel(String value) {
        this.diagnosticsLevel = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTrackingPropertiesType }
     *     
     */
    public ArrayOfTrackingPropertiesType getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTrackingPropertiesType }
     *     
     */
    public void setProperties(ArrayOfTrackingPropertiesType value) {
        this.properties = value;
    }

}
