//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.07 at 08:14:05 PM EET 
//


package com.ms.ews;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SyncFolderHierarchyChangesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyncFolderHierarchyChangesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Create" type="{http://schemas.microsoft.com/exchange/services/2006/types}SyncFolderHierarchyCreateOrUpdateType"/>
 *           &lt;element name="Update" type="{http://schemas.microsoft.com/exchange/services/2006/types}SyncFolderHierarchyCreateOrUpdateType"/>
 *           &lt;element name="Delete" type="{http://schemas.microsoft.com/exchange/services/2006/types}SyncFolderHierarchyDeleteType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyncFolderHierarchyChangesType", propOrder = {
    "createOrUpdateOrDelete"
})
public class SyncFolderHierarchyChangesType {

    @XmlElementRefs({
        @XmlElementRef(name = "Create", namespace = "http://schemas.microsoft.com/exchange/services/2006/types", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Update", namespace = "http://schemas.microsoft.com/exchange/services/2006/types", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Delete", namespace = "http://schemas.microsoft.com/exchange/services/2006/types", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> createOrUpdateOrDelete;

    /**
     * Gets the value of the createOrUpdateOrDelete property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the createOrUpdateOrDelete property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreateOrUpdateOrDelete().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link SyncFolderHierarchyCreateOrUpdateType }{@code >}
     * {@link JAXBElement }{@code <}{@link SyncFolderHierarchyDeleteType }{@code >}
     * {@link JAXBElement }{@code <}{@link SyncFolderHierarchyCreateOrUpdateType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getCreateOrUpdateOrDelete() {
        if (createOrUpdateOrDelete == null) {
            createOrUpdateOrDelete = new ArrayList<JAXBElement<?>>();
        }
        return this.createOrUpdateOrDelete;
    }

}
