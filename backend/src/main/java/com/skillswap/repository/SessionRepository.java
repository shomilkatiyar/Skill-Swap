package com.skillswap.repository;

import com.skillswap.entity.Session;
import com.skillswap.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findByTeacherIdOrLearnerIdOrderByScheduledAtDesc(UUID teacherId, UUID learnerId);
    List<Session> findByStatusAndReminderSentFalseAndScheduledAtBetween(
            SessionStatus status, LocalDateTime from, LocalDateTime to);
}
