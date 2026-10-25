package com.skillswap.repository;

import com.skillswap.entity.UserSkillWanted;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserSkillWantedRepository extends JpaRepository<UserSkillWanted, UUID> {
    List<UserSkillWanted> findByUserId(UUID userId);
    List<UserSkillWanted> findBySkillId(UUID skillId);
    void deleteByUserIdAndSkillId(UUID userId, UUID skillId);
}
