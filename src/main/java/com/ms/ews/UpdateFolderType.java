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
 * <p>Java class for UpdateFolderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateFolderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element name="FolderChanges" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyArrayOfFolderChangesType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateFolderType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "folderChanges"
})
public class UpdateFolderType
    extends BaseRequestType
{

    @XmlElement(name = "FolderChanges", required = true)
    protected NonEmptyArrayOfFolderChangesType folderChanges;

    /**
     * Gets the value of the folderChanges property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyArrayOfFolderChangesType }
     *     
     */
    public NonEmptyArrayOfFolderChangesType getFolderChanges() {
        return folderChanges;
    }

    /**
     * Sets the value of the folderChanges property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyArrayOfFolderChangesType }
     *     
     */
    public void setFolderChanges(NonEmptyArrayOfFolderChangesType value) {
        this.folderChanges = value;
    }

}
