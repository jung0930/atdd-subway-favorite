package nextstep.subway.application;

import nextstep.exception.NotFoundLineException;
import nextstep.subway.application.response.FindPathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private PathFinder pathFinder;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public FindPathResponse findShortestPath(Long startStationId, Long endStationId) {
        List<Line> lines = lineRepository.findAll();
        Station startStation = stationRepository.findById(startStationId)
                .orElseThrow(NotFoundLineException::new);
        Station endStation = stationRepository.findById(endStationId)
                .orElseThrow(NotFoundLineException::new);

        return pathFinder.findShortestPath(lines, startStation, endStation);
    }

}