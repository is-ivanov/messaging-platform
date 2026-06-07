package com.javaguru.messaging.chathistory.repository;

import com.javaguru.messaging.chathistory.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {

    /** Текущий максимум seq в чате (0, если сообщений ещё нет) — для назначения следующего номера. */
    @Query("select coalesce(max(m.seq), 0) from Message m where m.chatId = :chatId")
    long findMaxSeq(@Param("chatId") String chatId);

    /** Последние сообщения чата (без курсора): новейшие сверху. Один запрос, без N+1. */
    List<Message> findByChatIdOrderBySeqDesc(String chatId, Pageable pageable);

    /** Страница сообщений строго старше курсора beforeSeq: новейшие сверху. Один запрос, без N+1. */
    List<Message> findByChatIdAndSeqLessThanOrderBySeqDesc(String chatId, long beforeSeq, Pageable pageable);
}
