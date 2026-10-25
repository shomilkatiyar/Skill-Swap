package com.skillswap.service;

import com.skillswap.dto.CreateSessionRequest;
import com.skillswap.dto.SessionDto;
import com.skillswap.entity.*;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.SessionRepository;
import com.skillswap.repository.SkillRepository;
import com.skillswap.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final CreditService creditService;
    private final EmailService emailService;

    public SessionService(SessionRepository sessionRepository, UserRepository userRepository,
                           SkillRepository skillRepository, CreditService creditService,
                           EmailService emailService) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.creditService = creditService;
        this.emailService = emailService;
    }

    @Transactional
    public SessionDto createSession(UUID learnerId, CreateSessionRequest req) {
        if (learnerId.equals(req.getTeacherId())) {
            throw new ApiException("You can't book a session with yourself", HttpStatus.BAD_REQUEST);
        }

        User learner = userRepository.findById(learnerId)
                .orElseThrow(() -> new ApiException("Learner not found", HttpStatus.NOT_FOUND));
        User teacher = userRepository.findById(req.getTeacherId())
                .orElseThrow(() -> new ApiException("Teacher not found", HttpStatus.NOT_FOUND));
        Skill skill = skillRepository.findById(req.getSkillId())
                .orElseThrow(() -> new ApiException("Skill not found", HttpStatus.NOT_FOUND));

        Session session = new Session();
        session.setTeacher(teacher);
        session.setLearner(learner);
        session.setSkill(skill);
        session.setScheduledAt(req.getScheduledAt());
        session.setStatus(SessionStatus.PENDING);
        session = sessionRepository.save(session);

        emailService.sendBookingRequestEmail(teacher.getEmail(), teacher.getName(), learner.getName(), skill.getName());

        return toDto(session);
    }

    @Transactional
    public SessionDto updateStatus(UUID sessionId, UUID actingUserId, String newStatusStr) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException("Session not found", HttpStatus.NOT_FOUND));

        SessionStatus newStatus;
        try {
            newStatus = SessionStatus.valueOf(newStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid status", HttpStatus.BAD_REQUEST);
        }

        boolean isTeacher = session.getTeacher().getId().equals(actingUserId);
        boolean isLearner = session.getLearner().getId().equals(actingUserId);
        if (!isTeacher && !isLearner) {
            throw new ApiException("Not authorized to update this session", HttpStatus.FORBIDDEN);
        }

        // Only the teacher can confirm/reject a pending request
        if ((newStatus == SessionStatus.CONFIRMED || newStatus == SessionStatus.REJECTED) && !isTeacher) {
            throw new ApiException("Only the teacher can confirm or reject a request", HttpStatus.FORBIDDEN);
        }

        session.setStatus(newStatus);

        if (newStatus == SessionStatus.CONFIRMED) {
            // Credits move at confirmation time so the learner has committed and the
            // teacher has certainty the slot is worth holding.
            creditService.transferForSession(session);
        }

        if (newStatus == SessionStatus.COMPLETED) {
            emailService.sendReviewPromptEmail(session.getLearner().getEmail(), session.getLearner().getName(), session.getSkill().getName());
            emailService.sendReviewPromptEmail(session.getTeacher().getEmail(), session.getTeacher().getName(), session.getSkill().getName());
        } else {
            String recipientEmail = isTeacher ? session.getLearner().getEmail() : session.getTeacher().getEmail();
            String recipientName = isTeacher ? session.getLearner().getName() : session.getTeacher().getName();
            emailService.sendBookingStatusEmail(recipientEmail, recipientName, session.getSkill().getName(), newStatus.name());
        }

        session = sessionRepository.save(session);
        return toDto(session);
    }

    public List<SessionDto> getSessionsForUser(UUID userId) {
        return sessionRepository.findByTeacherIdOrLearnerIdOrderByScheduledAtDesc(userId, userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    /** Runs every 5 minutes; emails a reminder for sessions starting within the next 30 minutes. */
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void sendUpcomingReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Session> upcoming = sessionRepository.findByStatusAndReminderSentFalseAndScheduledAtBetween(
                SessionStatus.CONFIRMED, now, now.plusMinutes(30));

        for (Session s : upcoming) {
            emailService.sendSessionReminderEmail(s.getTeacher().getEmail(), s.getTeacher().getName(), s.getSkill().getName(), s.getScheduledAt());
            emailService.sendSessionReminderEmail(s.getLearner().getEmail(), s.getLearner().getName(), s.getSkill().getName(), s.getScheduledAt());
            s.setReminderSent(true);
            sessionRepository.save(s);
        }
    }

    private SessionDto toDto(Session s) {
        SessionDto dto = new SessionDto();
        dto.setId(s.getId());
        dto.setTeacherId(s.getTeacher().getId());
        dto.setTeacherName(s.getTeacher().getName());
        dto.setLearnerId(s.getLearner().getId());
        dto.setLearnerName(s.getLearner().getName());
        dto.setSkillId(s.getSkill().getId());
        dto.setSkillName(s.getSkill().getName());
        dto.setStatus(s.getStatus().name());
        dto.setScheduledAt(s.getScheduledAt());
        dto.setCreditCost(s.getCreditCost());
        return dto;
    }
}
