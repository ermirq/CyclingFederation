package com.zerogravitysolutions.core.categories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.commons.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDocument extends BaseDocument {

    private String categoryName;
    private String description;
    private String color;
    private Integer minAge;
    private Integer maxAge;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(final Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(final Integer maxAge) {
        this.maxAge = maxAge;
    }

    public CategoryDocument deepCopy () {
        final CategoryDocument copy = new CategoryDocument();

        copy.setId(this.getId());
        copy.setCategoryName(this.getCategoryName());
        copy.setDescription(this.getDescription());
        copy.setColor(this.getColor());
        copy.setMaxAge(this.getMaxAge());
        copy.setMinAge(this.getMinAge());

        return copy;
    }
}
