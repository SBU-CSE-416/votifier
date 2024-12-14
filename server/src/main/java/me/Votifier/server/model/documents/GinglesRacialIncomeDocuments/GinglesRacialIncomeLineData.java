package me.Votifier.server.model.documents.GinglesRacialIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class GinglesRacialIncomeLineData {
    public GinglesRacialIncomeLineData() {
        // No-args constructor
    }
    @Field("WHITE")
    private Lines WHITE;

    @Field("BLACK")
    private Lines BLACK;

    @Field("ASIAN")
    private Lines ASIAN;

    @Field("HISPANIC")
    private Lines HISPANIC;

    // Getters and Setters
    public Lines getWHITE() {
        return WHITE;
    }

    public void setWHITE(Lines WHITE) {
        this.WHITE = WHITE;
    }

    public Lines getBLACK() {
        return BLACK;
    }

    public void setBLACK(Lines BLACK) {
        this.BLACK = BLACK;
    }

    public Lines getASIAN() {
        return ASIAN;
    }

    public void setASIAN(Lines ASIAN) {
        this.ASIAN = ASIAN;
    }

    public Lines getHISPANIC() {
        return HISPANIC;
    }

    public void setHISPANIC(Lines HISPANIC) {
        this.HISPANIC = HISPANIC;
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
