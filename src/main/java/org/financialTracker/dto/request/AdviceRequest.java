package org.financialTracker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdviceRequest {

    private String category;
    private double amount;

}
