package nextstep.subway.application.dto;

import nextstep.subway.domain.Section;

import java.util.Objects;

public class ShowLineSectionDto {

    private StationDto upStation;
    private StationDto downStation;
    private Integer distance;

    private ShowLineSectionDto() {
    }

    private ShowLineSectionDto(StationDto upStation, StationDto downStation, Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static ShowLineSectionDto from(Section section) {
        return new ShowLineSectionDto(
                StationDto.from(section.getUpStation()),
                StationDto.from(section.getDownStation()),
                section.getDistance()
        );
    }

    public StationDto getUpStation() {
        return upStation;
    }

    public StationDto getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowLineSectionDto that = (ShowLineSectionDto) o;
        return Objects.equals(upStation, that.upStation) && Objects.equals(downStation, that.downStation) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }

}
