package com.javaguru.messaging.groups.repository;

import com.javaguru.messaging.groups.domain.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatRepository extends JpaRepository<GroupChat, String> {
}
