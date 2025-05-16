
package org.example.edumanagementservice.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeWeightDTO {
    private String assessmentType;

    @Min(0)
    @Max(100)
    private double weight;

    private String itemName;

    // 新增构造函数，方便只传 itemName 和 weight
    public GradeWeightDTO(String itemName, double weight) {
        this.itemName = itemName;
        this.weight = weight;
    }
}

