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
 * <p>Java class for SearchFolderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchFolderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}FolderType">
 *       &lt;sequence>
 *         &lt;element name="SearchParameters" type="{http://schemas.microsoft.com/exchange/services/2006/types}SearchParametersType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchFolderType", propOrder = {
    "searchParameters"
})
public class SearchFolderType
    extends FolderType
{

    @XmlElement(name = "SearchParameters")
    protected SearchParametersType searchParameters;

    /**
     * Gets the value of the searchParameters property.
     * 
     * @return
     *     possible object is
     *     {@link SearchParametersType }
     *     
     */
    public SearchParametersType getSearchParameters() {
        return searchParameters;
    }

    /**
     * Sets the value of the searchParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchParametersType }
     *     
     */
    public void setSearchParameters(SearchParametersType value) {
        this.searchParameters = value;
    }

}