package com.javaguru.messaging.groups.repository;

import com.javaguru.messaging.groups.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, String> {

    List<Channel> findByIdIn(Collection<String> ids);
}
