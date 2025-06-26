package com.zerogravitysolutions.core.categories;

import com.zerogravitysolutions.core.commons.BaseDTO;

public class CategoryDTO extends BaseDTO {

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
}
