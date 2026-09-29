package adaptivelearning.module.certificates.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CertificateRequest", description = "Request body for creating/updating a certificate")
public class CertificateRequest {

    @Schema(description = "Certificate name", example = "IELTS")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(description = "Unique certificate code", example = "IELTS")
    @NotBlank(message = "Code must not be blank")
    private String code;

    @Schema(description = "Certificate description", example = "International English Language Testing System")
    private String description;

    @Schema(description = "Total score", example = "9.0")
    private double totalScore;

    @Schema(description = "Passing score threshold", example = "5.0")
    private double passingScore;

    @Schema(description = "Passing band score", example = "5.0")
    private double passingBand;

    @Schema(description = "Skills as JSON array string", example = "[\"LISTENING\",\"READING\",\"WRITING\",\"SPEAKING\"]")
    private String skills;
}
