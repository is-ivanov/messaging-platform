package com.javaguru.messaging.connection.domain;

/**
 * Тип чата для ключа партиции Chat History (как в chat-history-service, слайд 78).
 * GROUP покрывает и личный чат 1-на-1 (группа из двух участников).
 */
public enum ChatType {
    GROUP,
    CHANNEL
}
