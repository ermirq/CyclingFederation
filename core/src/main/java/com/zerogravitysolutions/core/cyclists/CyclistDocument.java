package com.zerogravitysolutions.core.cyclists;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.categories.CategoryDocument;
import com.zerogravitysolutions.core.commons.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "cyclist")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CyclistDocument extends BaseDocument {

        private String firstName;

        private String lastName;

        private String email;

        private Integer age;

        private String gender;

        private String nationality;

        private String clubId;

        private CategoryDocument category;


        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(final String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(final String lastName) {
                this.lastName = lastName;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(final String email) {
                this.email = email;
        }

        public Integer getAge() {
                return age;
        }

        public void setAge(final Integer age) {
                this.age = age;
        }

        public String getNationality() {
                return nationality;
        }

        public void setNationality(final String nationality) {
                this.nationality = nationality;
        }

        public String getClubId() {
                return clubId;
        }

        public void setClubId(final String clubId) {
                this.clubId = clubId;
        }

        public String getGender() {
                return gender;
        }

        public void setGender(final String gender) {
                this.gender = gender;
        }

        public CategoryDocument getCategory() {
                return category;
        }

        public void setCategory(final CategoryDocument category) {
                this.category = category;
        }

        public CyclistDocument deepCopy (){
                final CyclistDocument copy = new CyclistDocument();

                copy.setId(this.getId());
                copy.setFirstName(this.getFirstName());
                copy.setLastName(this.getLastName());
                copy.setEmail(this.getEmail());
                copy.setAge(this.getAge());
                copy.setGender(this.getGender());
                copy.setNationality(this.getNationality());
                copy.setClubId(this.getClubId());
                copy.setCategory(this.getCategory());

                return copy;
        }
}
