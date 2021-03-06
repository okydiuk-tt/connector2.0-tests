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
 * <p>Java class for GetSharingMetadataResponseMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSharingMetadataResponseMessageType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="EncryptedSharedFolderDataCollection" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfEncryptedSharedFolderDataType"/>
 *         &lt;element name="InvalidRecipients" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfInvalidRecipientsType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSharingMetadataResponseMessageType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "encryptedSharedFolderDataCollection",
    "invalidRecipients"
})
public class GetSharingMetadataResponseMessageType
    extends ResponseMessageType
{

    @XmlElement(name = "EncryptedSharedFolderDataCollection")
    protected ArrayOfEncryptedSharedFolderDataType encryptedSharedFolderDataCollection;
    @XmlElement(name = "InvalidRecipients")
    protected ArrayOfInvalidRecipientsType invalidRecipients;

    /**
     * Gets the value of the encryptedSharedFolderDataCollection property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEncryptedSharedFolderDataType }
     *     
     */
    public ArrayOfEncryptedSharedFolderDataType getEncryptedSharedFolderDataCollection() {
        return encryptedSharedFolderDataCollection;
    }

    /**
     * Sets the value of the encryptedSharedFolderDataCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEncryptedSharedFolderDataType }
     *     
     */
    public void setEncryptedSharedFolderDataCollection(ArrayOfEncryptedSharedFolderDataType value) {
        this.encryptedSharedFolderDataCollection = value;
    }

    /**
     * Gets the value of the invalidRecipients property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInvalidRecipientsType }
     *     
     */
    public ArrayOfInvalidRecipientsType getInvalidRecipients() {
        return invalidRecipients;
    }

    /**
     * Sets the value of the invalidRecipients property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInvalidRecipientsType }
     *     
     */
    public void setInvalidRecipients(ArrayOfInvalidRecipientsType value) {
        this.invalidRecipients = value;
    }

}
