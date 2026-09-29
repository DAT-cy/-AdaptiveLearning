package adaptivelearning.module.adaptive.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreForecastResponse {

    private Long certificateId;
    private Double forecastScore;
    private Double confidence;
    private String reasoning;
}
