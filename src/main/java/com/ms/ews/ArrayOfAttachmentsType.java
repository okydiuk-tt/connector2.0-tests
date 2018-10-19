//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.07 at 08:14:05 PM EET 
//


package com.ms.ews;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfAttachmentsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfAttachmentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="ItemAttachment" type="{http://schemas.microsoft.com/exchange/services/2006/types}ItemAttachmentType"/>
 *         &lt;element name="FileAttachment" type="{http://schemas.microsoft.com/exchange/services/2006/types}FileAttachmentType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfAttachmentsType", propOrder = {
    "itemAttachmentOrFileAttachment"
})
public class ArrayOfAttachmentsType {

    @XmlElements({
        @XmlElement(name = "ItemAttachment", type = ItemAttachmentType.class),
        @XmlElement(name = "FileAttachment", type = FileAttachmentType.class)
    })
    protected List<AttachmentType> itemAttachmentOrFileAttachment;

    /**
     * Gets the value of the itemAttachmentOrFileAttachment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemAttachmentOrFileAttachment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemAttachmentOrFileAttachment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemAttachmentType }
     * {@link FileAttachmentType }
     * 
     * 
     */
    public List<AttachmentType> getItemAttachmentOrFileAttachment() {
        if (itemAttachmentOrFileAttachment == null) {
            itemAttachmentOrFileAttachment = new ArrayList<AttachmentType>();
        }
        return this.itemAttachmentOrFileAttachment;
    }

}
