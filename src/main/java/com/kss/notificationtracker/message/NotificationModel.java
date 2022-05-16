package com.kss.notificationtracker.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel implements Serializable {
    String id;
    String title;
    String template;
    String body;
    String url;
    String lstFileAttach;
    Map<String, String> data;
    String user;
    List<String> listUser;
    String topic;
    ProviderType providerType;
    MessageType messageType;

    public enum ProviderType {
        FIRE_BASE, SMS, EMAIL
    }

    public enum MessageType {
        TOPIC, SINGLE, MULTICAST
    }
}
