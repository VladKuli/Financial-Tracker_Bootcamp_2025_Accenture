package org.financialTracker.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class CreateCategoryDTO {
    @NotNull
    private String title;
    private String description;
    private String icon;
}
