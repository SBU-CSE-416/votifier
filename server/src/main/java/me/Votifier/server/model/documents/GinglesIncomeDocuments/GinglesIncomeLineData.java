package me.Votifier.server.model.documents.GinglesIncomeDocuments;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

public class GinglesIncomeLineData {
    public GinglesIncomeLineData() {
        // No-args constructor
    }
    @Field("ALL")
    private Lines ALL;

    @Field("RURAL")
    private Lines RURAL;

    @Field("SUBURBAN")
    private Lines SUBURBAN;

    @Field("URBAN")
    private Lines URBAN;

    // Getters and Setters
    public Lines getALL() {
        return ALL;
    }

    public void setALL(Lines ALL) {
        this.ALL = ALL;
    }

    public Lines getRURAL() {
        return RURAL;
    }

    public void setRURAL(Lines RURAL) {
        this.RURAL = RURAL;
    }

    public Lines getSUBURBAN() {
        return SUBURBAN;
    }

    public void setSUBURBAN(Lines SUBURBAN) {
        this.SUBURBAN = SUBURBAN;
    }

    public Lines getURBAN() {
        return URBAN;
    }

    public void setURBAN(Lines URBAN) {
        this.URBAN = URBAN;
    }

    public static class Lines {
        @Field("democratic")
        private LineData democratic;

        @Field("republican")
        private LineData republican;

        // Getters and Setters
        public LineData getDemocratic() {
            return democratic;
        }

        public void setDemocratic(LineData democratic) {
            this.democratic = democratic;
        }

        public LineData getRepublican() {
            return republican;
        }

        public void setRepublican(LineData republican) {
            this.republican = republican;
        }
    }
    public static class LineData {
        @Field("x")
        private List<Double> x;

        @Field("y")
        private List<Double> y;

        // Getters and Setters
        public List<Double> getX() {
            return x;
        }

        public void setX(List<Double> x) {
            this.x = x;
        }

        public List<Double> getY() {
            return y;
        }

        public void setY(List<Double> y) {
            this.y = y;
        }
    }
}
