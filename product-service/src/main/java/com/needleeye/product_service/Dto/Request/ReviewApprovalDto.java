package com.needleeye.product_service.Dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ReviewApprovalDto {
    @NotNull(message = "isApproved is required")
    @JsonProperty("isApproved")
    private Boolean isApproved;

    public ReviewApprovalDto() {
    }

    public ReviewApprovalDto(@NotNull(message = "isApproved is required") Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }
}
