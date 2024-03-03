package com.med.firebase.repository;

import com.med.firebase.dto.NotificationDTO;
import com.med.firebase.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("""
    SELECT new com.med.firebase.dto.NotificationDTO (
        id,
        title,
        body,
        clickAction,
        isRead,
        sentOn
    )
    FROM Notification 
    WHERE userId = :userId
    ORDER BY id DESC
    """)
    Page<NotificationDTO> getNotificationByUserId(Integer userId, Pageable pageable);

    @Query("""
    SELECT COUNT(id) 
    FROM Notification 
    WHERE userId = :userId AND isRead = false 
    """)
    Long countUnreadByUserId(Integer userId);

}
