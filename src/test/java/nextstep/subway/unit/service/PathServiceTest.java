package nextstep.subway.unit.service;

import nextstep.common.Constant;
import nextstep.exception.NotFoundStationException;
import nextstep.exception.SameFindPathStationsException;
import nextstep.exception.UnconnectedFindPathStationsException;
import nextstep.subway.application.LineService;
import nextstep.subway.application.PathService;
import nextstep.subway.application.dto.ShowStationDto;
import nextstep.subway.application.request.AddSectionRequest;
import nextstep.subway.application.response.FindPathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private LineService lineService;
    @Autowired
    private PathService pathService;

    private Long 교대역_ID;
    private Long 논현역_ID;
    private Long 신논현역_ID;
    private Long 강남역_ID;
    private Long 양재역_ID;
    private Long 남부터미널역_ID;
    private Long 압구정로데오역_ID;
    private Long 강남구청역_ID;
    private Long 을지로입구역_ID;

    private Long 이호선_ID;
    private Long 삼호선_ID;
    private Long 신분당선_ID;
    private Long 수인분당선_ID;

    private AddSectionRequest 교대역_강남역_구간;
    private AddSectionRequest 교대역_남부터미널역_구간;
    private AddSectionRequest 남부터미널역_양재역_구간;
    private AddSectionRequest 논현역_신논현역_구간;
    private AddSectionRequest 신논현역_강남역_구간;
    private AddSectionRequest 강남역_양재역_구간;
    private AddSectionRequest 압구정로데오역_강남구청역_구간;

    @BeforeEach
    protected void setUp() {
        교대역_ID = stationRepository.save(Station.from(Constant.교대역)).getStationId();
        논현역_ID = stationRepository.save(Station.from(Constant.논현역)).getStationId();
        신논현역_ID = stationRepository.save(Station.from(Constant.신논현역)).getStationId();
        강남역_ID = stationRepository.save(Station.from(Constant.강남역)).getStationId();
        양재역_ID = stationRepository.save(Station.from(Constant.양재역)).getStationId();
        남부터미널역_ID = stationRepository.save(Station.from(Constant.남부터미널역)).getStationId();
        압구정로데오역_ID = stationRepository.save(Station.from(Constant.압구정로데오역)).getStationId();
        강남구청역_ID = stationRepository.save(Station.from(Constant.강남구청역)).getStationId();
        을지로입구역_ID = stationRepository.save(Station.from(Constant.을지로입구역)).getStationId();

        이호선_ID = lineRepository.save(Line.of(Constant.이호선, Constant.초록색)).getLineId();
        삼호선_ID = lineRepository.save(Line.of(Constant.삼호선, Constant.주황색)).getLineId();
        신분당선_ID = lineRepository.save(Line.of(Constant.신분당선, Constant.빨간색)).getLineId();
        수인분당선_ID = lineRepository.save(Line.of(Constant.수인분당선, Constant.노란색)).getLineId();

        교대역_강남역_구간 = AddSectionRequest.of(교대역_ID, 강남역_ID, Constant.역_간격_10);
        교대역_남부터미널역_구간 = AddSectionRequest.of(교대역_ID, 남부터미널역_ID, Constant.역_간격_10);
        남부터미널역_양재역_구간 = AddSectionRequest.of(남부터미널역_ID, 양재역_ID, Constant.역_간격_15);
        논현역_신논현역_구간 = AddSectionRequest.of(논현역_ID, 신논현역_ID, Constant.역_간격_15);
        신논현역_강남역_구간 = AddSectionRequest.of(신논현역_ID, 강남역_ID, Constant.역_간격_10);
        강남역_양재역_구간 = AddSectionRequest.of(강남역_ID, 양재역_ID, Constant.역_간격_10);
        압구정로데오역_강남구청역_구간 = AddSectionRequest.of(압구정로데오역_ID, 강남구청역_ID, Constant.역_간격_10);

        lineService.addSection(이호선_ID, 교대역_강남역_구간);
        lineService.addSection(삼호선_ID, 교대역_남부터미널역_구간);
        lineService.addSection(삼호선_ID, 남부터미널역_양재역_구간);
        lineService.addSection(신분당선_ID, 논현역_신논현역_구간);
        lineService.addSection(신분당선_ID, 신논현역_강남역_구간);
        lineService.addSection(신분당선_ID, 강남역_양재역_구간);
        lineService.addSection(수인분당선_ID, 압구정로데오역_강남구청역_구간);
    }

    @DisplayName("같은 출발역과 도착역의 최단 경로를 조회한다.")
    @Test
    void 같은_노선의_출발역과_도착역의_최단_경로_조회() {
        // when
        FindPathResponse 교대역_양재역_경로_조회_응답 = pathService.findShortestPath(논현역_ID, 강남역_ID);

        // then
        지하철_경로_조회_검증(교대역_양재역_경로_조회_응답, List.of(Constant.논현역, Constant.신논현역, Constant.강남역), 논현역_신논현역_구간.getDistance() + 신논현역_강남역_구간.getDistance());
    }

    @DisplayName("여러 출발역과 도착역의 최단 경로를 조회한다.")
    @Test
    void 여러_노선의_출발역과_도착역의_최단_경로_조회() {
        // when
        FindPathResponse 교대역_양재역_경로_조회_응답 = pathService.findShortestPath(교대역_ID, 양재역_ID);

        // then
        지하철_경로_조회_검증(교대역_양재역_경로_조회_응답, List.of(Constant.교대역, Constant.강남역, Constant.양재역), 교대역_강남역_구간.getDistance() + 강남역_양재역_구간.getDistance());
    }

    @DisplayName("출발역과 도착역이 동일할 경우 경로 조회시 예외발생가 발생한다.")
    @Test
    void 출발역과_도착역이_동일하게_경로_조회시_예외발생() {
        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(양재역_ID, 양재역_ID))
                .isInstanceOf(SameFindPathStationsException.class);
        ;
    }

    /**
     * When 존재하지 않은 출발역이나 도착역을 조회 할 경우
     * Then 경로가 조회되지 않는다.
     */
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우 예외발생")
    @Test
    void 존재하지_않은_출발역이나_도착역_경로_조회시_예외발생() {
        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(양재역_ID, 을지로입구역_ID))
                .isInstanceOf(NotFoundStationException.class);
    }

    /**
     * When 출발역과 도착역이 연결이 되어 있지 않은 경우
     * Then 경로가 조회되지 않는다.
     */
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 경로를 조회할 수 없다.")
    @Test
    void 연결되지_않은_출발역과_도착역_경로_조회시_예외발생() {
        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(양재역_ID, 압구정로데오역_ID))
                .isInstanceOf(UnconnectedFindPathStationsException.class);
    }

    void 지하철_경로_조회_검증(FindPathResponse findPathResponse, List<String> stationNames, int distance) {
        List<ShowStationDto> stations = findPathResponse.getStations();

        assertThat(findPathResponse.getDistance()).isEqualTo(distance);
        assertThat(stations).hasSize(stationNames.size());
        assertThat(stations.stream()
                .map(stationDto -> stationDto.getName())
                .collect(Collectors.toList())
        ).isEqualTo(stationNames);
    }

}
