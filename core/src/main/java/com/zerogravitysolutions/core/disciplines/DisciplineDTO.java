package com.zerogravitysolutions.core.disciplines;

import com.zerogravitysolutions.core.commons.BaseDTO;

public class DisciplineDTO extends BaseDTO {

    private String code;
    private String title;
    private String description;
    private String validation;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(final String validation) {
        this.validation = validation;
    }
}
