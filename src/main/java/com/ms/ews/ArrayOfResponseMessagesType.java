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
 * <p>Java class for ArrayOfResponseMessagesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfResponseMessagesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="CreateItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="DeleteItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="GetItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="UpdateItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}UpdateItemResponseMessageType"/>
 *         &lt;element name="SendItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="DeleteFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="EmptyFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="CreateFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="GetFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="FindFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FindFolderResponseMessageType"/>
 *         &lt;element name="UpdateFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="MoveFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="CopyFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="CreateAttachmentResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}AttachmentInfoResponseMessageType"/>
 *         &lt;element name="DeleteAttachmentResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}DeleteAttachmentResponseMessageType"/>
 *         &lt;element name="GetAttachmentResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}AttachmentInfoResponseMessageType"/>
 *         &lt;element name="UploadItemsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}UploadItemsResponseMessageType"/>
 *         &lt;element name="ExportItemsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ExportItemsResponseMessageType"/>
 *         &lt;element name="FindItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FindItemResponseMessageType"/>
 *         &lt;element name="MoveItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="CopyItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="ResolveNamesResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResolveNamesResponseMessageType"/>
 *         &lt;element name="ExpandDLResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ExpandDLResponseMessageType"/>
 *         &lt;element name="GetServerTimeZonesResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetServerTimeZonesResponseMessageType"/>
 *         &lt;element name="GetEventsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetEventsResponseMessageType"/>
 *         &lt;element name="GetStreamingEventsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetStreamingEventsResponseMessageType"/>
 *         &lt;element name="SubscribeResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SubscribeResponseMessageType"/>
 *         &lt;element name="UnsubscribeResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="SendNotificationResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SendNotificationResponseMessageType"/>
 *         &lt;element name="SyncFolderHierarchyResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SyncFolderHierarchyResponseMessageType"/>
 *         &lt;element name="SyncFolderItemsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SyncFolderItemsResponseMessageType"/>
 *         &lt;element name="CreateManagedFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="ConvertIdResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ConvertIdResponseMessageType"/>
 *         &lt;element name="GetSharingMetadataResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetSharingMetadataResponseMessageType"/>
 *         &lt;element name="RefreshSharingFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}RefreshSharingFolderResponseMessageType"/>
 *         &lt;element name="GetSharingFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetSharingFolderResponseMessageType"/>
 *         &lt;element name="CreateUserConfigurationResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="DeleteUserConfigurationResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="GetUserConfigurationResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetUserConfigurationResponseMessageType"/>
 *         &lt;element name="UpdateUserConfigurationResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="GetRoomListsResponse" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetRoomListsResponseMessageType"/>
 *         &lt;element name="GetRoomsResponse" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetRoomsResponseMessageType"/>
 *         &lt;element name="ApplyConversationActionResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="FindMailboxStatisticsByKeywordsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FindMailboxStatisticsByKeywordsResponseMessageType"/>
 *         &lt;element name="GetPasswordExpirationDateResponse" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetPasswordExpirationDateResponseMessageType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfResponseMessagesType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage"
})
public class ArrayOfResponseMessagesType {

    @XmlElementRefs({
        @XmlElementRef(name = "DeleteItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CreateAttachmentResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ResolveNamesResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ConvertIdResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CreateItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "FindFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SyncFolderItemsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "EmptyFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "MoveFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ApplyConversationActionResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SubscribeResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UpdateItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SyncFolderHierarchyResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "RefreshSharingFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ExpandDLResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetEventsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UpdateUserConfigurationResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CopyItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "FindMailboxStatisticsByKeywordsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UpdateFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetRoomsResponse", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CreateFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UnsubscribeResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CopyFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "MoveItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetPasswordExpirationDateResponse", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CreateManagedFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetAttachmentResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DeleteAttachmentResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetStreamingEventsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ExportItemsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "FindItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SendNotificationResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetServerTimeZonesResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetSharingMetadataResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SendItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetRoomListsResponse", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetUserConfigurationResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DeleteFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UploadItemsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GetSharingFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DeleteUserConfigurationResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CreateUserConfigurationResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<? extends ResponseMessageType>> createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage;

    /**
     * Gets the value of the createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttachmentInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResolveNamesResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConvertIdResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FindFolderResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link SyncFolderItemsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link SubscribeResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link UpdateItemResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link SyncFolderHierarchyResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefreshSharingFolderResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpandDLResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetEventsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FindMailboxStatisticsByKeywordsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetRoomsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetPasswordExpirationDateResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttachmentInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link DeleteAttachmentResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetStreamingEventsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExportItemsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FindItemResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link SendNotificationResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetServerTimeZonesResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetSharingMetadataResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetRoomListsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetUserConfigurationResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link UploadItemsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetSharingFolderResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends ResponseMessageType>> getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage() {
        if (createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage == null) {
            createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage = new ArrayList<JAXBElement<? extends ResponseMessageType>>();
        }
        return this.createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage;
    }

}
