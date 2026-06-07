package com.javaguru.messaging.groups.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Таблица Channels (слайд 74): channel_id, channel_name, timestamp, owner_id, channel_url.
 */
@Entity
@Table(name = "channels")
public class Channel {

    @Id
    @Column(name = "channel_id")
    private String id;

    @Column(name = "channel_name", nullable = false)
    private String channelName;

    @Column(name = "`timestamp`", nullable = false)
    private Instant timestamp;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "channel_url", nullable = false)
    private String channelUrl;

    protected Channel() {
        // for JPA
    }

    public Channel(String id, String channelName, Instant timestamp, String ownerId, String channelUrl) {
        this.id = id;
        this.channelName = channelName;
        this.timestamp = timestamp;
        this.ownerId = ownerId;
        this.channelUrl = channelUrl;
    }

    public String getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getChannelUrl() {
        return channelUrl;
    }
}
