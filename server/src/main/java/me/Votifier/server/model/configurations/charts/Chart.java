package me.Votifier.server.model.configurations.charts;

public class Chart {
    private String chartTitle;

    private String chartAxisXLabel;
    private String chartAxisXUnitLabel;
    private int[] chartAxisXMarkers;

    private String chartAxisYLabel;
    private String chartAxisYUnitLabel;
    private int[] chartAxisYMarkers;

    public Chart(
        String chartTitle, 
        String chartAxisXLabel, 
        String chartAxisXUnitLabel, 
        int[] chartAxisXMarkers,
        String chartAxisYLabel, 
        String chartAxisYUnitLabel, 
        int[] chartAxisYMarkers
        ) {
        this.chartTitle = chartTitle;
        this.chartAxisXLabel = chartAxisXLabel;
        this.chartAxisXUnitLabel = chartAxisXUnitLabel;
        this.chartAxisXMarkers = chartAxisXMarkers;
        this.chartAxisYLabel = chartAxisYLabel;
        this.chartAxisYUnitLabel = chartAxisYUnitLabel;
        this.chartAxisYMarkers = chartAxisYMarkers;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }
    
    public String getChartAxisXLabel() {
        return chartAxisXLabel;
    }

    public void setChartAxisXLabel(String chartAxisXLabel) {
        this.chartAxisXLabel = chartAxisXLabel;
    }

    public String getChartAxisXUnitLabel() {
        return chartAxisXUnitLabel;
    }

    public void setChartAxisXUnitLabel(String chartAxisXUnitLabel) {
        this.chartAxisXUnitLabel = chartAxisXUnitLabel;
    }

    public int[] getChartAxisXMarkers() {
        return chartAxisXMarkers;
    }

    public void setChartAxisXMarkers(int[] chartAxisXMarkers) {
        this.chartAxisXMarkers = chartAxisXMarkers;
    }

    public String getChartAxisYLabel() {
        return chartAxisYLabel;
    }

    public void setChartAxisYLabel(String chartAxisYLabel) {
        this.chartAxisYLabel = chartAxisYLabel;
    }

    public String getChartAxisYUnitLabel() {
        return chartAxisYUnitLabel;
    }

    public void setChartAxisYUnitLabel(String chartAxisYUnitLabel) {
        this.chartAxisYUnitLabel = chartAxisYUnitLabel;
    }

    public int[] getChartAxisYMarkers() {
        return chartAxisYMarkers;
    }

    public void setChartAxisYMarkers(int[] chartAxisYMarkers) {
        this.chartAxisYMarkers = chartAxisYMarkers;
    }
}