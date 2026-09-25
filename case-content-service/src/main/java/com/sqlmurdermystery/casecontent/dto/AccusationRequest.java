package com.sqlmurdermystery.casecontent.dto;

import jakarta.validation.constraints.NotNull;

public class AccusationRequest {

    @NotNull(message = "suspectId is required")
    private Integer suspectId;

    private String reasoning;   // optional: the learner's explanation / query they used

    public AccusationRequest() {}

    public Integer getSuspectId() { return suspectId; }
    public void setSuspectId(Integer suspectId) { this.suspectId = suspectId; }

    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
}
