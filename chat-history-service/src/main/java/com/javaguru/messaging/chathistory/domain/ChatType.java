package com.javaguru.messaging.chathistory.domain;

/**
 * Тип чата, которому принадлежит сообщение.
 * На слайде 78 это выражено двумя раздельными полями документа: group_id | channel_id.
 * Здесь сведено к дискриминатору (chatType) + единому chatId — один из двух санкционированных
 * планом (§4.3) вариантов. GROUP покрывает и личный чат 1-на-1 (группа из двух участников).
 */
public enum ChatType {
    GROUP,
    CHANNEL
}
