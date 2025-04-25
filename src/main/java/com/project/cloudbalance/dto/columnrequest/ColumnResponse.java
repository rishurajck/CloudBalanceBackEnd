package com.project.cloudbalance.dto.columnrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnResponse {
    private Long id;
    private String displayName;
    private String actualName;
}
