package com.javaguru.messaging.groups.repository;

import com.javaguru.messaging.groups.domain.ChannelSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelSubscriberRepository extends JpaRepository<ChannelSubscriber, Long> {

    List<ChannelSubscriber> findByChannelId(String channelId);

    boolean existsByChannelIdAndUserId(String channelId, String userId);

    @Query("select cs.channelId from ChannelSubscriber cs where cs.userId = :userId")
    List<String> findChannelIdsByUserId(@Param("userId") String userId);
}
