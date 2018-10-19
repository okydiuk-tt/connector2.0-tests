package com.timetrade.ews.notifications.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ParsedNotification {

    private final String subscriptionId;
    private final String waterMark;
    private final Set<ItemInFolder> itemsInFolder;

    public ParsedNotification(String subscriptionId, String waterMark, Set<ItemInFolder> itemsInFolder) {
        this.subscriptionId = subscriptionId;
        this.waterMark = waterMark;
        this.itemsInFolder = itemsInFolder;
    }

    public static ParsedNotification statusEvent(String subscriptionId, String waterMark) {
        return new ParsedNotification(subscriptionId, waterMark, Collections.emptySet());
    }

    public boolean isStatusEvent() {
        return itemsInFolder.isEmpty();
    }

    public static class ItemInFolder {
        private final String id;
        private final FolderId folderId;

        public ItemInFolder(String id, FolderId folderId) {
            this.id = id;
            this.folderId = folderId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, folderId);
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if((obj == null) || (getClass() != obj.getClass()))
                return false;

            ItemInFolder iif = (ItemInFolder)obj;

            return Objects.equals(id, iif.id) && Objects.equals(folderId, iif.folderId);
        }

        public String getId() {
            return id;
        }
        public FolderId getFolderId() {
            return folderId;
        }
    }

    public static class FolderId {
        private final String id;
        private final String changeKey;

        public FolderId(String id, String changeKey) {
            this.id = id;
            this.changeKey = changeKey;
        }

        public String getId() {
            return id;
        }
        public String getChangeKey() {
            return changeKey;
        }
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getWaterMark() {
        return waterMark;
    }

    public Set<ItemInFolder> getItemsInFolder() {
        return itemsInFolder;
    }

}
