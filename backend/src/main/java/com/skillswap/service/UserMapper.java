package com.skillswap.service;

import com.skillswap.dto.SkillMiniDto;
import com.skillswap.dto.UserDto;
import com.skillswap.entity.User;

import java.util.List;

public class UserMapper {
    public static UserDto toDto(User user, List<SkillMiniDto> offered, List<SkillMiniDto> wanted) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setLocation(user.getLocation());
        dto.setCreditsBalance(user.getCreditsBalance());
        dto.setSkillsOffered(offered);
        dto.setSkillsWanted(wanted);
        return dto;
    }
}
