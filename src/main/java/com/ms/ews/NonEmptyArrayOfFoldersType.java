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
 * <p>Java class for NonEmptyArrayOfFoldersType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NonEmptyArrayOfFoldersType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Folder" type="{http://schemas.microsoft.com/exchange/services/2006/types}FolderType"/>
 *         &lt;element name="CalendarFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}CalendarFolderType"/>
 *         &lt;element name="ContactsFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}ContactsFolderType"/>
 *         &lt;element name="SearchFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}SearchFolderType"/>
 *         &lt;element name="TasksFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}TasksFolderType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NonEmptyArrayOfFoldersType", propOrder = {
    "folderOrCalendarFolderOrContactsFolder"
})
public class NonEmptyArrayOfFoldersType {

    @XmlElements({
        @XmlElement(name = "Folder", type = FolderType.class),
        @XmlElement(name = "CalendarFolder", type = CalendarFolderType.class),
        @XmlElement(name = "ContactsFolder", type = ContactsFolderType.class),
        @XmlElement(name = "SearchFolder", type = SearchFolderType.class),
        @XmlElement(name = "TasksFolder", type = TasksFolderType.class)
    })
    protected List<BaseFolderType> folderOrCalendarFolderOrContactsFolder;

    /**
     * Gets the value of the folderOrCalendarFolderOrContactsFolder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folderOrCalendarFolderOrContactsFolder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolderOrCalendarFolderOrContactsFolder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FolderType }
     * {@link CalendarFolderType }
     * {@link ContactsFolderType }
     * {@link SearchFolderType }
     * {@link TasksFolderType }
     * 
     * 
     */
    public List<BaseFolderType> getFolderOrCalendarFolderOrContactsFolder() {
        if (folderOrCalendarFolderOrContactsFolder == null) {
            folderOrCalendarFolderOrContactsFolder = new ArrayList<BaseFolderType>();
        }
        return this.folderOrCalendarFolderOrContactsFolder;
    }

}
