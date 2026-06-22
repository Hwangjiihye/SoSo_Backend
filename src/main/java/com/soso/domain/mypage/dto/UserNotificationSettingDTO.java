package com.soso.domain.mypage.dto;

public class UserNotificationSettingDTO {
    private Long settingSeq;
    private Long storeSeq;
    private String notificationType;
    private String channelType;
    private String isEnabled;

    public UserNotificationSettingDTO() {}

    public UserNotificationSettingDTO(Long storeSeq, String notificationType, String channelType, String isEnabled) {
        this.storeSeq = storeSeq;
        this.notificationType = notificationType;
        this.channelType = channelType;
        this.isEnabled = isEnabled;
    }

    public Long getSettingSeq() {
        return settingSeq;
    }

    public void setSettingSeq(Long settingSeq) {
        this.settingSeq = settingSeq;
    }

    public Long getStoreSeq() {
        return storeSeq;
    }

    public void setStoreSeq(Long storeSeq) {
        this.storeSeq = storeSeq;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }
}
