package nextstep.subway.application.response;

import nextstep.subway.application.dto.ShowStationDto;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FindPathResponse {

    private List<ShowStationDto> stations;

    private Integer distance;

    private FindPathResponse() {
    }

    private FindPathResponse(List<ShowStationDto> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static FindPathResponse of(List<Station> stations, Integer distance) {
        return new FindPathResponse(
                stations.stream()
                        .map(ShowStationDto::from)
                        .collect(Collectors.toList()),
                distance
        );
    }

    public List<ShowStationDto> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindPathResponse that = (FindPathResponse) o;
        return stations.equals(that.stations) && distance.equals(that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }

}