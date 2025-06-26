package com.zerogravitysolutions.core.races;

import com.zerogravitysolutions.core.categories.CategoryDTO;
import com.zerogravitysolutions.core.commons.BaseDTO;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import com.zerogravitysolutions.core.disciplines.DisciplineDTO;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import com.zerogravitysolutions.core.volunteers.VolunteersDTO;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RaceDTO extends BaseDTO {

    private String raceName;
    private LocalDate raceDate;
    private LocalTime startTime;
    private LocalTime estimatedDuration;
    private double distance;
    private List<CheckpointDocument> checkpoints;
    private double elevationGain;
    private String location;
    private String mapLink;
    private List<CategoriesDTO> categories;
    private List<ParticipantsDTO> participants = new ArrayList<>();
    private List<VolunteersDTO> volunteers;
    private List<SponsorDTO> sponsors;
    private List<LogisticsDTO> logistics;
    private DisciplineDTO discipline;

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

    public List<CategoriesDTO> getCategories() {
        return categories;
    }

    public void setCategories(final List<CategoriesDTO> categories) {
        this.categories = categories;
    }

    public List<ParticipantsDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(final List<ParticipantsDTO> participants) {
        this.participants = participants;
    }

    public List<VolunteersDTO> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(final List<VolunteersDTO> volunteers) {
        this.volunteers = volunteers;
    }

    public List<SponsorDTO> getSponsors() {
        return sponsors;
    }

    public void setSponsors(final List<SponsorDTO> sponsors) {
        this.sponsors = sponsors;
    }

    public List<LogisticsDTO> getLogistics() {
        return logistics;
    }

    public void setLogistics(final List<LogisticsDTO> logistics) {
        this.logistics = logistics;
    }


    public DisciplineDTO getDiscipline() {
        return discipline;
    }

    public void setDiscipline(final DisciplineDTO discipline) {
        this.discipline = discipline;
    }

    public static class SponsorDTO{

        private ObjectId id;
        private String name;
        private byte[] logo;
        private String contribution;
        private String contactInfo;
        private String email;

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

    public static class LogisticsDTO {

        private ObjectId id;
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

    public static class CategoriesDTO{
        private CategoryDTO categoryId;
        private Integer minNumber;
        private Integer maxNumber;

        public CategoryDTO getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(final CategoryDTO categoryId) {
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

    public static class ParticipantsDTO {
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
