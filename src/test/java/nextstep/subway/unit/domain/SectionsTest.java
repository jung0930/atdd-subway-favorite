package nextstep.subway.unit.domain;

import nextstep.exception.AlreadyExistSectionException;
import nextstep.exception.NotFoundStationException;
import nextstep.exception.NotFoundUpStationOrDownStation;
import nextstep.subway.common.Constant;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class SectionsTest {

    private Line 신분당선;
    private Station 논현역;
    private Station 신논현역;
    private Station 강남역;
    private Station 양재역;
    private Station 신사역;
    private Section 논현_신논현_구간;
    private Section 신논현_강남_구간;
    private Section 논현_강남_구간;
    private Section 강남_신논현_구간;
    private Section 신사_논현_구간;
    private Section 강남_양재_구간;

    @BeforeEach
    protected void setUp() {
        신분당선 = Line.of(Constant.신분당선, Constant.빨간색);
        논현역 = Station.from(Constant.논현역);
        신논현역 = Station.from(Constant.신논현역);
        강남역 = Station.from(Constant.강남역);
        양재역 = Station.from(Constant.양재역);
        신사역 = Station.from(Constant.신사역);
        논현_신논현_구간 = Section.of(논현역, 신논현역, Constant.역_간격_10);
        신논현_강남_구간 = Section.of(신논현역, 강남역, Constant.역_간격_10);
        논현_강남_구간 = Section.of(논현역, 강남역, Constant.역_간격_20);
        강남_신논현_구간 = Section.of(강남역, 신논현역, Constant.역_간격_10);
        신사_논현_구간 = Section.of(신사역, 논현역, Constant.역_간격_10);
        강남_양재_구간 = Section.of(강남역, 양재역, Constant.역_간격_10);
    }

    @DisplayName("노선 마지막에 구간 등록")
    @Test
    void 노선_마지막에_구간_등록() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));

        // when
        신분당선_구간들.addSection(신논현_강남_구간);

        // then
        List<Section> 구간들 = 신분당선_구간들.getSortedSections();
        구간_변화_검증(구간들, 2, List.of(논현_신논현_구간, 신논현_강남_구간), Constant.역_간격_20);
    }

    @DisplayName("노선 중간에 구간 등록")
    @Test
    void 노선_중간에_구간_등록() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_강남_구간)));

        // when
        신분당선_구간들.addSection(논현_신논현_구간);

        // then
        List<Section> 구간들 = 신분당선_구간들.getSortedSections();
        구간_변화_검증(구간들, 2, List.of(논현_신논현_구간, 신논현_강남_구간), Constant.역_간격_20);
    }

    @DisplayName("노선 처음에 구간 등록")
    @Test
    void 노선_처음에_구간_등록() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));

        // when
        신분당선_구간들.addSection(신사_논현_구간);

        // then
        List<Section> 구간들 = 신분당선_구간들.getSortedSections();
        구간_변화_검증(구간들, 2, List.of(신사_논현_구간, 논현_신논현_구간), Constant.역_간격_20);
    }

    @DisplayName("이미 등록된 구간을 등록하면 예외발생")
    @Test
    void 이미_등록된_구간을_등록하면_예외발생() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));

        // when & then
        assertThatThrownBy(() -> 신분당선_구간들.addSection(논현_신논현_구간))
                .isInstanceOf(AlreadyExistSectionException.class);
    }

    @DisplayName("상행역과 하행역이 모두 노선에 없는 구간을 등록하면 예외발생")
    @Test
    void 상행역과_하행역이_모두_노선에_없는_구간을_등록하면_예외발생() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));

        // when & then
        assertThatThrownBy(() -> 신분당선_구간들.addSection(강남_양재_구간))
                .isInstanceOf(NotFoundUpStationOrDownStation.class);
    }

    @DisplayName("노선 마지막 구간 삭제")
    @Test
    void 노선_마지막_구간_삭제() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));
        신분당선_구간들.addSection(신논현_강남_구간);

        // when
        신분당선_구간들.deleteSection(강남역);

        // then
        List<Section> 구간들 = 신분당선_구간들.getSortedSections();
        구간_변화_검증(구간들, 1, List.of(논현_신논현_구간), Constant.역_간격_10);
    }

    @DisplayName("노선 중간 구간 삭제")
    @Test
    void 노선_중간_구간_삭제() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));
        신분당선_구간들.addSection(신논현_강남_구간);

        // when
        신분당선_구간들.deleteSection(신논현역);

        // then
        List<Section> 구간들 = 신분당선_구간들.getSortedSections();
        구간_변화_검증(구간들, 1, List.of(논현_강남_구간), Constant.역_간격_20);
    }

    @DisplayName("노선 처음 구간 삭제")
    @Test
    void 노선_처음_구간_삭제() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));
        신분당선_구간들.addSection(신논현_강남_구간);

        // when
        신분당선_구간들.deleteSection(논현역);

        // then
        List<Section> 구간들 = 신분당선_구간들.getSortedSections();
        구간_변화_검증(구간들, 1, List.of(신논현_강남_구간), Constant.역_간격_10);
    }

    @DisplayName("상행역과 하행역이 모두 노선에 없는 구간을 제거하면 예외발생")
    @Test
    void 상행역과_하행역이_모두_노선에_없는_구간을_등록하면_제거발생() {
        // given
        Sections 신분당선_구간들 = Sections.from(new LinkedList(Arrays.asList(논현_신논현_구간)));

        // when & then
        assertThatThrownBy(() -> 신분당선_구간들.deleteSection(강남역))
                .isInstanceOf(NotFoundStationException.class);
    }

    void 구간_변화_검증(List<Section> 구간들, int 구간_수, List<Section> 비교_구간들, int 노선_길이) {
        assertThat(구간들).hasSize(구간_수);
        assertThat(구간들).isEqualTo(비교_구간들);
        assertThat(구간들.stream().mapToInt(Section::getDistance).sum()).isEqualTo(노선_길이);
    }

}