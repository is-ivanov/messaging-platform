package com.javaguru.messaging.groups.repository;

import com.javaguru.messaging.groups.domain.GroupParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {

    List<GroupParticipant> findByGroupId(String groupId);

    @Query("select gp.groupId from GroupParticipant gp where gp.userId = :userId")
    List<String> findGroupIdsByUserId(@Param("userId") String userId);

    /**
     * Поиск группы с ТОЧНО таким набором участников (для lazy-resolve, в т.ч. 1-на-1):
     * группа, у которой ровно :size участников и все они входят в :ids.
     * Используется, чтобы повторные сообщения тем же людям переиспользовали тот же чат.
     */
    @Query("""
            select gp.groupId from GroupParticipant gp
            group by gp.groupId
            having count(gp) = :size
               and sum(case when gp.userId in :ids then 1 else 0 end) = :size
            """)
    List<String> findGroupIdsWithExactParticipants(@Param("ids") Collection<String> ids,
                                                    @Param("size") long size);
}
