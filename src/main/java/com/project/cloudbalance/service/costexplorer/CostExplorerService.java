package com.project.cloudbalance.service.costexplorer;

import com.project.cloudbalance.config.SnowflakeConfig;
import com.project.cloudbalance.dto.columnrequest.DynamicCostRequest;
import com.project.cloudbalance.dto.columnrequest.ColumnResponse;
import com.project.cloudbalance.entity.Columns;
import com.project.cloudbalance.repository.ColumnRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CostExplorerService {

    @Autowired
    private SnowflakeConfig snowflakeConfig;
    @Autowired
    private ColumnRespository columnRespository;
    @Autowired
    @Qualifier("snowflakeDataSource")
    private DataSource snowflakeDataSource;


    // getting the column names
    public ResponseEntity<List<ColumnResponse>> getColumns() {
        List<Columns> columns = columnRespository.findAll();

        List<ColumnResponse> responseList = columns.stream().map(column -> {
            ColumnResponse response = new ColumnResponse();
            response.setId(column.getId());
            response.setDisplayName(column.getDisplayName());
            response.setActualName(column.getActualName());
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }


    // Just return all results as is
    public List<Map<String, Object>> downloadData(DynamicCostRequest request, String groupByDisplayName) {
        return fetchCostData(request, groupByDisplayName);
    }
    // Process Top 5 + Others after fetching
    public List<Map<String, Object>> fetchDynamicData(DynamicCostRequest request, String groupByDisplayName) {
        List<Map<String, Object>> results = fetchCostData(request, groupByDisplayName);

        if (results.isEmpty()) {
            return results;
        }

        String groupByColumn = columnRespository.findByDisplayName(groupByDisplayName)
                .map(Columns::getActualName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupBy value: " + groupByDisplayName));

        results.sort((a, b) -> Double.compare(
                ((Number) b.get("TOTAL_USAGE_COST")).doubleValue(),
                ((Number) a.get("TOTAL_USAGE_COST")).doubleValue()
        ));

        List<Map<String, Object>> top5 = results.stream().limit(5).collect(Collectors.toList());

        if (results.size() > 5) {
            double othersCost = results.stream()
                    .skip(5)
                    .mapToDouble(r -> ((Number) r.get("TOTAL_USAGE_COST")).doubleValue())
                    .sum();

            Map<String, Object> othersRow = new LinkedHashMap<>();
            othersRow.put("USAGE_MONTH", results.get(0).get("USAGE_MONTH")); // Assuming all months are the same
            othersRow.put(groupByColumn, "Others");
            othersRow.put("TOTAL_USAGE_COST", othersCost);

            top5.add(othersRow);
        }

        return top5;
    }
    // Common method to run the query
    private List<Map<String, Object>> fetchCostData(DynamicCostRequest request, String groupByDisplayName) {
        List<Map<String, Object>> results = new ArrayList<>();

        String groupByColumn = columnRespository.findByDisplayName(groupByDisplayName)
                .map(Columns::getActualName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupBy value: " + groupByDisplayName));

        StringBuilder query = new StringBuilder();
        query.append("SELECT TO_CHAR(USAGESTARTDATE, 'YYYY-MM') AS USAGE_MONTH, ")
                .append(groupByColumn).append(", ")
                .append("SUM(LINEITEM_USAGEAMOUNT) AS TOTAL_USAGE_COST ")
                .append("FROM COST_EXPLORER WHERE USAGESTARTDATE BETWEEN ? AND ? ");

        List<Object> params = new ArrayList<>();

        LocalDate start = LocalDate.parse(request.getStartMonth() + "-01");
        LocalDate end = LocalDate.parse(request.getEndMonth() + "-01").withDayOfMonth(1).plusMonths(1).minusDays(1);

        params.add(Date.valueOf(start));
        params.add(Date.valueOf(end));

        Map<String, Object> filters = request.getFilters();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof List) {
                List<?> values = (List<?>) value;
                if (!values.isEmpty()) {
                    query.append("AND ").append(key).append(" IN (")
                            .append(String.join(", ", Collections.nCopies(values.size(), "?")))
                            .append(") ");
                    params.addAll(values);
                }
            } else {
                query.append("AND ").append(key).append(" = ? ");
                params.add(value);
            }
        }

        query.append("GROUP BY TO_CHAR(USAGESTARTDATE, 'YYYY-MM'), ").append(groupByColumn).append(" ");
        query.append("ORDER BY USAGE_MONTH, TOTAL_USAGE_COST DESC");

        try (Connection conn = snowflakeDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= colCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Snowflake query failed: " + e.getMessage(), e);
        }

        return results;
    }


    // get column Data
    public List<Map<String, Object>> fetchColumnData(String displayName) {
        List<Map<String, Object>> results = new ArrayList<>();

        // Fetch the actual DB column name using displayName
        String actualColumnName = columnRespository.findByDisplayName(displayName)
                .map(Columns::getActualName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid column name: " + displayName));

        String sql = "SELECT DISTINCT " + actualColumnName + " FROM COST_EXPLORER LIMIT 50";

        try (Connection conn = snowflakeDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                results.add(row);
            }

        } catch (Exception e) {
            throw new RuntimeException("Snowflake query failed: " + e.getMessage(), e);
        }

        return results;
    }

}
