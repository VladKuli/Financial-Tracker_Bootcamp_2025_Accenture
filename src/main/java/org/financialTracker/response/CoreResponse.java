package org.financialTracker.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class CoreResponse extends CoreError {

    private List<CoreError> errorList = new ArrayList<>();

    public List<CoreError> getErrors() {
        return errorList;
    }

}
