package com.sqlmurdermystery.casecontent.service;

import com.sqlmurdermystery.casecontent.dto.AccusationRequest;
import com.sqlmurdermystery.casecontent.dto.AccusationResultDto;
import com.sqlmurdermystery.casecontent.dto.CaseDetailDto;
import com.sqlmurdermystery.casecontent.dto.CaseSummaryDto;
import com.sqlmurdermystery.casecontent.model.CaseFile;
import com.sqlmurdermystery.casecontent.repository.CaseFileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CaseService {

    private static final int PREVIEW_LENGTH = 160;

    /** The `solution` table has no per-case points column, so every case awards the same. */
    private static final int POINTS_PER_CASE = 100;

    private final CaseFileRepository caseFileRepository;
    private final JdbcTemplate jdbcTemplate;

    public CaseService(CaseFileRepository caseFileRepository, JdbcTemplate jdbcTemplate) {
        this.caseFileRepository = caseFileRepository;
        this.jdbcTemplate = jdbcTemplate;
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
     * Grades an accusation against the `solution` table (case_id, culprit_person_id,
     * motive, key_evidence) in this service's own database. The caseId is the
     * crime_scene_report.case_id the learner opened. Nothing is persisted here; the
     * frontend records progress once it sees {@code correct == true}.
     */
    @Transactional(readOnly = true)
    public AccusationResultDto submitAccusation(Long caseId, AccusationRequest request) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT culprit_person_id, motive, key_evidence FROM solution WHERE case_id = ?",
                caseId);

        if (rows.isEmpty()) {
            throw new EntityNotFoundException("No solution on file for case " + caseId);
        }

        Map<String, Object> solution = rows.get(0);
        Number culprit = (Number) solution.get("culprit_person_id");

        boolean correct = culprit != null
                && request.getSuspectId() != null
                && culprit.intValue() == request.getSuspectId();

        if (correct) {
            return new AccusationResultDto(
                    true,
                    "Case closed! Your query-writing cracked it.",
                    explanation((String) solution.get("motive"), (String) solution.get("key_evidence")),
                    POINTS_PER_CASE
            );
        }

        return new AccusationResultDto(
                false,
                "Not quite — that's not who the evidence points to. Re-check the clues and try again.",
                null,
                null
        );
    }

    private String explanation(String motive, String keyEvidence) {
        StringBuilder sb = new StringBuilder();
        if (motive != null && !motive.isBlank()) sb.append("Motive: ").append(motive.trim());
        if (keyEvidence != null && !keyEvidence.isBlank()) {
            if (sb.length() > 0) sb.append("\n");
            sb.append("Key evidence: ").append(keyEvidence.trim());
        }
        return sb.toString();
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