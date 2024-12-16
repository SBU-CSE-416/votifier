package me.Votifier.server.model.documents.BoxplotRegionDocuments;

import org.springframework.data.mongodb.core.mapping.Field;



    public class BoxPlotStats {
        public BoxPlotStats() {
            // No-args constructor
        }
        @Field("MIN")
        private int MIN;

        @Field("LOWER_QUARTILE_Q1")
        private int LOWER_QUARTILE_Q1;

        @Field("MEDIAN")
        private int MEDIAN;

        @Field("UPPER_QUARTILE_Q3")
        private int UPPER_QUARTILE_Q3;

        @Field("MAX")
        private int MAX;

        @Field("2022_DOT_VALUE")
        private int _2022_DOT_VALUE;

        // Getters and Setters for each field
        public int getMIN() {
            return MIN;
        }

        public void setMIN(int MIN) {
            this.MIN = MIN;
        }

        public int getLOWER_QUARTILE_Q1() {
            return LOWER_QUARTILE_Q1;
        }

        public void setLOWER_QUARTILE_Q1(int LOWER_QUARTILE_Q1) {
            this.LOWER_QUARTILE_Q1 = LOWER_QUARTILE_Q1;
        }

        public int getMEDIAN() {
            return MEDIAN;
        }

        public void setMEDIAN(int MEDIAN) {
            this.MEDIAN = MEDIAN;
        }

        public int getUPPER_QUARTILE_Q3() {
            return UPPER_QUARTILE_Q3;
        }

        public void setUPPER_QUARTILE_Q3(int UPPER_QUARTILE_Q3) {
            this.UPPER_QUARTILE_Q3 = UPPER_QUARTILE_Q3;
        }

        public int getMAX() {
            return MAX;
        }

        public void setMAX(int MAX) {
            this.MAX = MAX;
        }

        public int get_2022_DOT_VALUE() {
            return _2022_DOT_VALUE;
        }

        public void set_2022_DOT_VALUE(int _2022_DOT_VALUE) {
            this._2022_DOT_VALUE = _2022_DOT_VALUE;
        }
}
