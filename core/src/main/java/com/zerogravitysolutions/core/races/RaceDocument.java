package com.zerogravitysolutions.core.races;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.categories.CategoryDocument;
import com.zerogravitysolutions.core.commons.BaseDocument;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import com.zerogravitysolutions.core.disciplines.DisciplineDocument;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import com.zerogravitysolutions.core.volunteers.VolunteersDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "races")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RaceDocument extends BaseDocument {

    private String raceName;
    private LocalDate raceDate;
    private LocalTime startTime;
    private LocalTime estimatedDuration;
    private double distance;
    private List<CheckpointDocument> checkpoints = new ArrayList<>();
    private double elevationGain;
    private String location;
    private String mapLink;
    private List<CategoriesDocument> categories = new ArrayList<>();
    private List<Participants> participants = new ArrayList<>();
    private List<VolunteersDocument> volunteers = new ArrayList<>();
    private List<Sponsor> sponsors;
    private List<Logistics> logistics;
    private DisciplineDocument discipline;

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(final String raceName) {
        this.raceName = raceName;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(final LocalDate raceDate) {
        this.raceDate = raceDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(final LocalTime estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public List<CheckpointDocument> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(final List<CheckpointDocument> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(final double elevationGain) {
        this.elevationGain = elevationGain;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(final String mapLink) {
        this.mapLink = mapLink;
    }

    public List<CategoriesDocument> getCategories() {
        return categories;
    }

    public void setCategories(final List<CategoriesDocument> categories) {
        this.categories = categories;
    }

    public List<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(final List<Participants> participants) {
        this.participants = participants;
    }

    public List<VolunteersDocument> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(final List<VolunteersDocument> volunteers) {
        this.volunteers = volunteers;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(final List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public List<Logistics> getLogistics() {
        return logistics;
    }

    public void setLogistics(final List<Logistics> logistics) {
        this.logistics = logistics;
    }


    public DisciplineDocument getDiscipline() {
        return discipline;
    }

    public void setDiscipline(final DisciplineDocument discipline) {
        this.discipline = discipline;
    }

    public RaceDocument deepCopy(){
        final RaceDocument copy = new RaceDocument();

        copy.setId(this.getId());
        copy.setRaceName(this.getRaceName());
        copy.setRaceDate(this.getRaceDate());
        copy.setStartTime(this.getStartTime());
        copy.setEstimatedDuration(this.getEstimatedDuration());
        copy.setDistance(this.getDistance());
        copy.setElevationGain(this.getElevationGain());
        copy.setLocation(this.getLocation());
        copy.setMapLink(this.getMapLink());
        copy.setCategories(this.getCategories());
        copy.setParticipants(this.getParticipants());
        copy.setLogistics(this.getLogistics());
        copy.setSponsors(this.getSponsors());
        copy.setVolunteers(this.getVolunteers());
        copy.setDiscipline(this.getDiscipline());

        return copy;
    }

    public static class Sponsor {

        private ObjectId id = new ObjectId();
        private String name;
        @JsonIgnore
        private byte[] logo;
        private String contribution;
        private String contactInfo;
        private String email;

//        logo.png/.jpeg-store ne db max 2MB
        public ObjectId getId() {
            return id;
        }

        public void setId(final ObjectId id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public byte[] getLogo() {
            return logo;
        }

        public void setLogo(final byte[] logo) {
            this.logo = logo;
        }

        public String getContribution() {
            return contribution;
        }

        public void setContribution(final String contribution) {
            this.contribution = contribution;
        }

        public String getContactInfo() {
            return contactInfo;
        }

        public void setContactInfo(final String contactInfo) {
            this.contactInfo = contactInfo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(final String email) {
            this.email = email;
        }
    }

    public static class Logistics {

        private ObjectId id = new ObjectId();
        private String title;
        private String details;

        public ObjectId getId() {
            return id;
        }

        public void setId(final ObjectId id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(final String details) {
            this.details = details;
        }
    }

    public static class CategoriesDocument {

        private CategoryDocument categoryId;
        private Integer minNumber;
        private Integer maxNumber;

        public CategoryDocument getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(final CategoryDocument categoryId) {
            this.categoryId = categoryId;
        }

        public Integer getMinNumber() {
            return minNumber;
        }

        public void setMinNumber(final Integer minNumber) {
            this.minNumber = minNumber;
        }

        public Integer getMaxNumber() {
            return maxNumber;
        }

        public void setMaxNumber(final Integer maxNumber) {
            this.maxNumber = maxNumber;
        }
    }

    public static class Participants {

        private CyclistDocument cyclistId;
        private Integer raceNumber;
        private String categoryId;
        private Integer startingPosition;

        public CyclistDocument getCyclistId() {
            return cyclistId;
        }

        public void setCyclistId(final CyclistDocument cyclistId) {
            this.cyclistId = cyclistId;
        }

        public Integer getRaceNumber() {
            return raceNumber;
        }

        public void setRaceNumber(final Integer raceNumber) {
            this.raceNumber = raceNumber;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(final String categoryId) {
            this.categoryId = categoryId;
        }

        public Integer getStartingPosition() {
            return startingPosition;
        }

        public void setStartingPosition(final Integer startingPosition) {
            this.startingPosition = startingPosition;
        }
    }
}