package com.placement.repository;

import com.placement.entity.RecruitmentRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRoundRepository extends JpaRepository<RecruitmentRound, Long> {
    List<RecruitmentRound> findByDriveIdOrderByRoundOrderAsc(Long driveId);
}
