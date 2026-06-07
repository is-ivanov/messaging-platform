package com.javaguru.messaging.groups.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Таблица Channel_Subscribers (слайд 75): user_id, channel_id.
 */
@Entity
@Table(
        name = "channel_subscribers",
        uniqueConstraints = @UniqueConstraint(name = "uq_channel_user", columnNames = {"channel_id", "user_id"})
)
public class ChannelSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "channel_id", nullable = false)
    private String channelId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    protected ChannelSubscriber() {
        // for JPA
    }

    public ChannelSubscriber(String channelId, String userId) {
        this.channelId = channelId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUserId() {
        return userId;
    }
}
