package me.Votifier.server.model.documents.BoxplotRacialDocuments;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
public class BoxPlotLabel {
    public BoxPlotLabel() {
        // No-args constructor
    }

    @Field("title")
    private String title;

    @Field("subtitle")
    private String subtitle;

    @Field("axis-x")

    private String axisX;

    @Field("axis-y")

    private String axisY;

    @Field("axis-x-ticks")

    private List<Integer> axisXTicks;

    @Field("axis-y-ticks")

    private List<Integer> axisYTicks;

    @Field("legend")

    private BoxPlotLegend legend;

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAxisX() {
        return axisX;
    }

    public void setAxisX(String axisX) {
        this.axisX = axisX;
    }

    public String getAxisY() {
        return axisY;
    }

    public void setAxisY(String axisY) {
        this.axisY = axisY;
    }

    public List<Integer> getAxisXTicks() {
        return axisXTicks;
    }

    public void setAxisXTicks(List<Integer> axisXTicks) {
        this.axisXTicks = axisXTicks;
    }

    public List<Integer> getAxisYTicks() {
        return axisYTicks;
    }

    public void setAxisYTicks(List<Integer> axisYTicks) {
        this.axisYTicks = axisYTicks;
    }

    public BoxPlotLegend getLegend() {
        return legend;
    }

    public void setLegend(BoxPlotLegend legend) {
        this.legend = legend;
    }

    public static class BoxPlotLegend {
        @Field("enacted_dots")
        private String enactedDots;

        //getter and setter

        public String getEnactedDots() {
            return enactedDots;
        }

        public void setEnactedDots(String enactedDots) {
            this.enactedDots = enactedDots;
        }
    }
}
