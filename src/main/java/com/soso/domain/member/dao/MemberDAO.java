package com.soso.domain.member.dao;

import com.soso.domain.member.dto.SignUpDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDAO {
    /**
     * 사업자 회원가입 정보를 DB에 저장합니다.
     * @param signUpDto 가공 완료된 회원 정보 DTO
     * @return 성공 시 1, 실패 시 0
     */
    int insertMember(SignUpDto signUpDto);

    /**
     * 아이디 중복 여부를 확인합니다.
     * @param userId 확인할 아이디
     * @return 존재하면 1, 없으면 0
     */
    int countByUserId(String userId);

    /**
     * 닉네임 중복 여부를 확인합니다.
     * @param nickname 확인할 닉네임
     * @return 존재하면 1, 없으면 0
     */
    int countByNickname(String nickname);
}
