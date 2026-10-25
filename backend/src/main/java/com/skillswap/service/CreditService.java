package com.skillswap.service;

import com.skillswap.entity.*;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.CreditTransactionRepository;
import com.skillswap.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * All credit balance mutations MUST go through this service so that the
 * balance on the user row and the transaction ledger never drift apart.
 * Methods are @Transactional so a failure partway through rolls back cleanly.
 */
@Service
public class CreditService {

    private final UserRepository userRepository;
    private final CreditTransactionRepository transactionRepository;

    public CreditService(UserRepository userRepository, CreditTransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void recordBonus(User user, int amount, String description) {
        transactionRepository.save(new CreditTransaction(user, null, amount, TransactionType.BONUS, description));
    }

    /**
     * Moves credits from learner to teacher for a completed/confirmed session.
     * Throws if the learner doesn't have enough balance.
     */
    @Transactional
    public void transferForSession(Session session) {
        User learner = userRepository.findById(session.getLearner().getId())
                .orElseThrow(() -> new ApiException("Learner not found", HttpStatus.NOT_FOUND));
        User teacher = userRepository.findById(session.getTeacher().getId())
                .orElseThrow(() -> new ApiException("Teacher not found", HttpStatus.NOT_FOUND));

        int cost = session.getCreditCost();

        if (learner.getCreditsBalance() < cost) {
            throw new ApiException("Not enough credits to book this session", HttpStatus.BAD_REQUEST);
        }

        learner.setCreditsBalance(learner.getCreditsBalance() - cost);
        teacher.setCreditsBalance(teacher.getCreditsBalance() + cost);

        userRepository.save(learner);
        userRepository.save(teacher);

        String skillName = session.getSkill().getName();
        transactionRepository.save(new CreditTransaction(
                learner, session, -cost, TransactionType.SPENT, "Learned " + skillName));
        transactionRepository.save(new CreditTransaction(
                teacher, session, cost, TransactionType.EARNED, "Taught " + skillName));
    }

    public List<CreditTransaction> getHistory(UUID userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
