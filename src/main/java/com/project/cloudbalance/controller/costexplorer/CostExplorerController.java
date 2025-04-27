package com.project.cloudbalance.controller.costexplorer;

import com.project.cloudbalance.dto.columnrequest.DynamicCostRequest;
import com.project.cloudbalance.dto.columnrequest.ColumnResponse;
import com.project.cloudbalance.service.costexplorer.CostExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/snowflake")
public class CostExplorerController {

    @Autowired
    private CostExplorerService costExplorerService;

    @GetMapping("/column")
    public ResponseEntity<List<ColumnResponse>> getColumns() {
        return costExplorerService.getColumns();
    }


    @PostMapping("/dynamic-query")
    public ResponseEntity<List<Map<String, Object>>> getDynamicResults(
            @RequestParam String groupBy,
            @RequestBody DynamicCostRequest request) {
        return ResponseEntity.ok(costExplorerService.fetchDynamicData(request, groupBy));
    }

    @GetMapping("/columnDetails")
    public List<Map<String, Object>> getColumnData(@RequestParam String columnName) {
        return costExplorerService.fetchColumnData(columnName);
    }

    @PostMapping("/download")
    public ResponseEntity<List<Map<String, Object>>> download(
            @RequestParam String groupBy,
            @RequestBody DynamicCostRequest request)
    {
        return ResponseEntity.ok(costExplorerService.downloadData(request, groupBy));
    }


}
