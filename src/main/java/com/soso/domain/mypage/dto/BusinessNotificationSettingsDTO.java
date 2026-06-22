package com.soso.domain.mypage.dto;

import java.util.List;

public class BusinessNotificationSettingsDTO {
    private String alertStockYn;
    private String alertExpiryYn;
    private String alertOrderYn;
    private List<UserNotificationSettingDTO> settings;

    public BusinessNotificationSettingsDTO() {}

    public BusinessNotificationSettingsDTO(String alertStockYn, String alertExpiryYn, String alertOrderYn, List<UserNotificationSettingDTO> settings) {
        this.alertStockYn = alertStockYn;
        this.alertExpiryYn = alertExpiryYn;
        this.alertOrderYn = alertOrderYn;
        this.settings = settings;
    }

    public String getAlertStockYn() {
        return alertStockYn;
    }

    public void setAlertStockYn(String alertStockYn) {
        this.alertStockYn = alertStockYn;
    }

    public String getAlertExpiryYn() {
        return alertExpiryYn;
    }

    public void setAlertExpiryYn(String alertExpiryYn) {
        this.alertExpiryYn = alertExpiryYn;
    }

    public String getAlertOrderYn() {
        return alertOrderYn;
    }

    public void setAlertOrderYn(String alertOrderYn) {
        this.alertOrderYn = alertOrderYn;
    }

    public List<UserNotificationSettingDTO> getSettings() {
        return settings;
    }

    public void setSettings(List<UserNotificationSettingDTO> settings) {
        this.settings = settings;
    }
}
