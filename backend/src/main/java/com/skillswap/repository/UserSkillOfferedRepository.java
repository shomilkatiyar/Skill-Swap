package com.skillswap.repository;

import com.skillswap.entity.UserSkillOffered;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserSkillOfferedRepository extends JpaRepository<UserSkillOffered, UUID> {
    List<UserSkillOffered> findByUserId(UUID userId);
    List<UserSkillOffered> findBySkillId(UUID skillId);
    void deleteByUserIdAndSkillId(UUID userId, UUID skillId);
}
