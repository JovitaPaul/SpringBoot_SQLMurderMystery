package com.sqlmurdermystery.casecontent.service;

import com.sqlmurdermystery.casecontent.dto.AccusationRequest;
import com.sqlmurdermystery.casecontent.dto.AccusationResultDto;
import com.sqlmurdermystery.casecontent.dto.CaseDetailDto;
import com.sqlmurdermystery.casecontent.dto.CaseSummaryDto;
import com.sqlmurdermystery.casecontent.model.CaseFile;
import com.sqlmurdermystery.casecontent.repository.CaseFileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaseService {

    private static final int PREVIEW_LENGTH = 160;

    private final CaseFileRepository caseFileRepository;

    public CaseService(CaseFileRepository caseFileRepository) {
        this.caseFileRepository = caseFileRepository;
    }

    @Transactional(readOnly = true)
    public List<CaseSummaryDto> listCases() {
        return caseFileRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(c -> new CaseSummaryDto(
                        c.getId(),
                        c.getTitle(),
                        preview(c.getBriefing()),
                        c.getDifficulty().name(),
                        c.getPointsReward()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public CaseDetailDto getCase(Long caseId) {
        CaseFile caseFile = findOrThrow(caseId);
        return new CaseDetailDto(
                caseFile.getId(),
                caseFile.getTitle(),
                caseFile.getBriefing(),
                caseFile.getDifficulty().name(),
                caseFile.getTargetSchema(),
                caseFile.getPointsReward()
        );
    }

    /**
     * Grades an accusation. This is a pure check against {@link CaseFile#getSolutionSuspectId()}
     * — it does not persist anything or record progress. The caller (frontend) is
     * expected to call progress-tracking-service's completion endpoint once it sees
     * {@code correct == true}, so a learner's progress lives in exactly one place.
     */
    @Transactional(readOnly = true)
    public AccusationResultDto submitAccusation(Long caseId, AccusationRequest request) {
        CaseFile caseFile = findOrThrow(caseId);

        boolean correct = caseFile.getSolutionSuspectId().equals(request.getSuspectId());

        if (correct) {
            return new AccusationResultDto(
                    true,
                    "Case closed! Your query-writing cracked it.",
                    caseFile.getSolutionExplanation(),
                    caseFile.getPointsReward()
            );
        }

        return new AccusationResultDto(
                false,
                "Not quite — that's not who the evidence points to. Re-check the clues and try again.",
                null,
                null
        );
    }

    private CaseFile findOrThrow(Long caseId) {
        return caseFileRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Case " + caseId + " not found"));
    }

    private String preview(String briefing) {
        if (briefing == null) return "";
        if (briefing.length() <= PREVIEW_LENGTH) return briefing;
        return briefing.substring(0, PREVIEW_LENGTH).trim() + "...";
    }
}
