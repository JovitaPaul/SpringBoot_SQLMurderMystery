package com.sqlmurdermystery.queryexec.controller;

import com.sqlmurdermystery.queryexec.dto.QueryRequest;
import com.sqlmurdermystery.queryexec.dto.QueryResultDto;
import com.sqlmurdermystery.queryexec.service.QueryExecutionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryExecutionService queryExecutionService;

    public QueryController(QueryExecutionService queryExecutionService) {
        this.queryExecutionService = queryExecutionService;
    }

    /**
     * Runs a learner-submitted SELECT against the given case schema.
     * Requires auth (see SecurityConfig) purely to rate-limit/attribute usage — the
     * actual safety guarantees come from SqlSafetyValidator + the read-only DB account.
     */
    @PostMapping("/execute")
    public ResponseEntity<QueryResultDto> execute(@Valid @RequestBody QueryRequest request) throws SQLException {
        return ResponseEntity.ok(queryExecutionService.execute(request));
    }
}
