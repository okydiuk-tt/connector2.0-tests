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
 * <p>Java class for FindConversationResponseMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindConversationResponseMessageType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType">
 *       &lt;sequence>
 *         &lt;element name="Conversations" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfConversationsType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindConversationResponseMessageType", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", propOrder = {
    "conversations"
})
public class FindConversationResponseMessageType
    extends ResponseMessageType
{

    @XmlElement(name = "Conversations", required = true)
    protected ArrayOfConversationsType conversations;

    /**
     * Gets the value of the conversations property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfConversationsType }
     *     
     */
    public ArrayOfConversationsType getConversations() {
        return conversations;
    }

    /**
     * Sets the value of the conversations property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfConversationsType }
     *     
     */
    public void setConversations(ArrayOfConversationsType value) {
        this.conversations = value;
    }

}