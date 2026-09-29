package adaptivelearning.module.certificates.dto.response;

import adaptivelearning.shared.enums.ContentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CertificateResponse")
public class CertificateResponse {

    @Schema(description = "Certificate ID")
    private Long certificateId;

    @Schema(description = "Certificate name")
    private String name;

    @Schema(description = "Unique certificate code")
    private String code;

    @Schema(description = "Certificate description")
    private String description;

    @Schema(description = "Total score")
    private double totalScore;

    @Schema(description = "Passing score threshold")
    private double passingScore;

    @Schema(description = "Passing band score")
    private double passingBand;

    @Schema(description = "Skills as JSON array string")
    private String skills;

    @Schema(description = "Content status")
    private ContentStatus status;
}
