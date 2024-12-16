package me.Votifier.server.model.documents.PlanSplitsDocuments;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
public class PlanSplitsLabel {
    public PlanSplitsLabel() {
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

    private List<String> axisXTicks;

    @Field("axis-x-bar-colors")

    private List<String> axisXBarColors;

    @Field("axis-y-ticks")

    private List<Integer> axisYTicks;


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

    public List<String> getAxisXTicks() {
        return axisXTicks;
    }

    public void setAxisXTicks(List<String> axisXTicks) {
        this.axisXTicks = axisXTicks;
    }

    public List<Integer> getAxisYTicks() {
        return axisYTicks;
    }

    public void setAxisYTicks(List<Integer> axisYTicks) {
        this.axisYTicks = axisYTicks;
    }

}
