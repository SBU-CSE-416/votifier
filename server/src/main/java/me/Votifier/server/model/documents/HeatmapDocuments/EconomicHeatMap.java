
package me.Votifier.server.model.documents.HeatmapDocuments;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
@Document(collection = "economic_data")
public class EconomicHeatMap {

    
    public EconomicHeatMap() {
        // No-args constructor
    }
    @Field("NAME")
    private String NAME;

    @Field("data")
    private List<EconomicHeatMapData> data;

    //getter and setter

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<EconomicHeatMapData> getData() {
        return data;
    }

    public void setData(List<EconomicHeatMapData> data) {
        this.data = data;
    }

    public static class EconomicHeatMapData {
        public EconomicHeatMapData() {
            // No-args constructor
        }
        @Field("UNIQUE_ID")
        private String UNIQUE_ID;

        @Field("TOT_HOUS22")
        private String TOTAL_HOUSEHOLDS;

        @Field("0_35K")
        private String _0_35K;

        @Field("35_60K")

        private String _35_60K;

        @Field("60_100K")

        private String _60_100K;

        @Field("100K_125K")

        private String _100K_125K;

        @Field("125K_150K")

        private String _125K_150K;

        @Field("150K_MORE")

        private String _150K_MORE;
        
        // Getters and Setters

        public String getUNIQUE_ID() {
            return UNIQUE_ID;
        }

        public void setUNIQUE_ID(String UNIQUE_ID) {
            this.UNIQUE_ID = UNIQUE_ID;

        }

        public String get_0_35K() {
            return _0_35K;
        }

        public void set_0_35K(String _0_35K) {
            this._0_35K = _0_35K;
        }

        public String get_35_60K() {
            return _35_60K;
        }

        public void set_35_60K(String _35_60K) {
            this._35_60K = _35_60K;
        }

        public String get_60_100K() {
            return _60_100K;
        }

        public void set_60_100K(String _60_100K) {
            this._60_100K = _60_100K;
        }

        public String get_100K_125K() {
            return _100K_125K;
        }

        public void set_100K_125K(String _100K_125K) {
            this._100K_125K = _100K_125K;
        }

        public String get_125K_150K() {
            return _125K_150K;
        }

        public void set_125K_150K(String _125K_150K) {
            this._125K_150K = _125K_150K;
        }

        public String get_150K_MORE() {
            return _150K_MORE;
        }

        public void set_150K_MORE(String _150K_MORE) {
            this._150K_MORE = _150K_MORE;
        }

        public String getTOTAL_HOUSEHOLDS() {
            return TOTAL_HOUSEHOLDS;
        }

        public void setTOTAL_HOUSEHOLDS(String TOTAL_HOUSEHOLDS) {
            this.TOTAL_HOUSEHOLDS = TOTAL_HOUSEHOLDS;
        }
    }
}