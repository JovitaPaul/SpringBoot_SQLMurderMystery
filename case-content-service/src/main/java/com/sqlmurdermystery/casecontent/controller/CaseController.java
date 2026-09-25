package com.sqlmurdermystery.casecontent.controller;

import com.sqlmurdermystery.casecontent.dto.AccusationRequest;
import com.sqlmurdermystery.casecontent.dto.AccusationResultDto;
import com.sqlmurdermystery.casecontent.dto.CaseDetailDto;
import com.sqlmurdermystery.casecontent.dto.CaseSummaryDto;
import com.sqlmurdermystery.casecontent.service.CaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    /** Case-Solving Phase case list — public, no login required to browse. */
    @GetMapping
    public ResponseEntity<List<CaseSummaryDto>> listCases() {
        return ResponseEntity.ok(caseService.listCases());
    }

    /** Briefing + targetSchema (the schema query-execution-service should run against). */
    @GetMapping("/{caseId}")
    public ResponseEntity<CaseDetailDto> getCase(@PathVariable Long caseId) {
        return ResponseEntity.ok(caseService.getCase(caseId));
    }

    /** Submit a final accusation (who the learner thinks did it). Requires auth. */
    @PostMapping("/{caseId}/accusations")
    public ResponseEntity<AccusationResultDto> submitAccusation(@PathVariable Long caseId,
                                                                  @Valid @RequestBody AccusationRequest request) {
        return ResponseEntity.ok(caseService.submitAccusation(caseId, request));
    }
}
