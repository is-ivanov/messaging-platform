package com.javaguru.messaging.server.domain;

/** Тип отправки из WS-конверта {@code {type, payload}}. */
public enum MessageType {
    SEND_DIRECT,
    SEND_GROUP,
    SEND_CHANNEL
}
