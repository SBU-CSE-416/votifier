package me.Votifier.server.model.documents;
import org.springframework.data.mongodb.core.mapping.Field;
public class EnsemblesStateSummary {
    public EnsemblesStateSummary() {

    }

    @Field("NAME")
    private String NAME;

    @Field("PLANS")
    private Integer PLANS;

    @Field("MCMC_POP_EQUALITY_THRESHOLD")
    private Double MCMC_POP_EQUALITY_THRESHOLD;

    // Getters and Setters

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getPLANS() {
        return PLANS;
    }

    public void setPLANS(Integer PLANS) {
        this.PLANS = PLANS;
    }

    public Double getMCMC_POP_EQUALITY_THRESHOLD() {
        return MCMC_POP_EQUALITY_THRESHOLD;
    }

    public void setMCMC_POP_EQUALITY_THRESHOLD(Double MCMC_POP_EQUALITY_THRESHOLD) {
        this.MCMC_POP_EQUALITY_THRESHOLD = MCMC_POP_EQUALITY_THRESHOLD;
    }

}
