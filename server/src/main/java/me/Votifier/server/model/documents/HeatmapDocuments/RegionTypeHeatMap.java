
package me.Votifier.server.model.documents.HeatmapDocuments;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(collection = "region_type_data")
public class RegionTypeHeatMap {
    public RegionTypeHeatMap() {
        // No-args constructor
    }
    @Field("NAME")
    private String NAME;

    @Field("data")
    private List<RegionTypeHeatMapData> data;

    //getter and setter

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<RegionTypeHeatMapData> getData() {
        return data;
    }

    public void setData(List<RegionTypeHeatMapData> data) {
        this.data = data;
    }

    public static class RegionTypeHeatMapData {
        public RegionTypeHeatMapData() {
            // No-args constructor
        }
        @Field("UNIQUE_ID")
        private String UNIQUE_ID;
        @Field("region_type")
        private String region_type;
        // Getters and Setters

        public String getUNIQUE_ID() {
            return UNIQUE_ID;
        }

        public void setUNIQUE_ID(String UNIQUE_ID) {
            this.UNIQUE_ID = UNIQUE_ID;
        }

        public String getRegion_type() {
            return region_type;
        }

        public void setRegion_type(String region_type) {
            this.region_type = region_type;
        }
        
    }
}