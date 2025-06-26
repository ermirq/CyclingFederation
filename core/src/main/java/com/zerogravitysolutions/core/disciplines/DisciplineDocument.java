package com.zerogravitysolutions.core.disciplines;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.commons.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "disciplines")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisciplineDocument extends BaseDocument {

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

    public DisciplineDocument deepCopy (){
        final DisciplineDocument copy = new DisciplineDocument();

        copy.setId(this.getId());
        copy.setCode(this.getCode());
        copy.setTitle(this.getTitle());
        copy.setDescription(this.getDescription());
        copy.setValidation(this.getValidation());

        return copy;
    }
}
