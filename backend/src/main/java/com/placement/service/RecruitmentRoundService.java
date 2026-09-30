package com.placement.service;

import com.placement.dto.RecruitmentRoundRequest;
import com.placement.dto.RecruitmentRoundResponse;
import com.placement.entity.PlacementDrive;
import com.placement.entity.RecruitmentRound;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.PlacementDriveRepository;
import com.placement.repository.RecruitmentRoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruitmentRoundService {

    private final RecruitmentRoundRepository roundRepository;
    private final PlacementDriveRepository driveRepository;

    public RecruitmentRoundService(RecruitmentRoundRepository roundRepository,
                                   PlacementDriveRepository driveRepository) {
        this.roundRepository = roundRepository;
        this.driveRepository = driveRepository;
    }

    @Transactional(readOnly = true)
    public List<RecruitmentRoundResponse> getRoundsByDriveId(Long driveId) {
        return roundRepository.findByDriveIdOrderByRoundOrderAsc(driveId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecruitmentRoundResponse createRound(Long driveId, RecruitmentRoundRequest request) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found with id: " + driveId));

        RecruitmentRound round = new RecruitmentRound(
                drive,
                request.getRoundOrder(),
                request.getRoundName().trim(),
                request.getDescription(),
                request.getScheduledAt()
        );

        RecruitmentRound saved = roundRepository.save(round);
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteRound(Long roundId) {
        RecruitmentRound round = roundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruitment round not found with id: " + roundId));
        roundRepository.delete(round);
    }

    private RecruitmentRoundResponse mapToResponse(RecruitmentRound round) {
        RecruitmentRoundResponse response = new RecruitmentRoundResponse();
        response.setId(round.getId());
        response.setDriveId(round.getDrive().getId());
        response.setRoundOrder(round.getRoundOrder());
        response.setRoundName(round.getRoundName());
        response.setDescription(round.getDescription());
        response.setScheduledAt(round.getScheduledAt());
        return response;
    }
}
