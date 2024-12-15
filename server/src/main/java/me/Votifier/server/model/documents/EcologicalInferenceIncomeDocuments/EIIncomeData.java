package me.Votifier.server.model.documents.EcologicalInferenceIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;
public class EIIncomeData {
    public EIIncomeData() {
        // No-args constructor
    }

    @Field("0_35K")
    private EIIncomePartyData _0_35K;

    @Field("35K_60K")
    private EIIncomePartyData _35K_60K;

    @Field("60K_100K")

    private EIIncomePartyData _60K_100K;

    @Field("100K_125K")
    private EIIncomePartyData _100K_125K;

    @Field("125K_150K")

    private EIIncomePartyData _125K_150K;

    @Field("150K_MORE")

    private EIIncomePartyData _150K_MORE;

    // Getters and Setters

    public EIIncomePartyData get_0_35K() {
        return _0_35K;
    }

    public void set_0_35K(EIIncomePartyData _0_35K) {
        this._0_35K = _0_35K;
    }

    public EIIncomePartyData get_35K_60K() {
        return _35K_60K;
    }

    public void set_35K_60K(EIIncomePartyData _35K_60K) {
        this._35K_60K = _35K_60K;
    }

    public EIIncomePartyData get_60K_100K() {
        return _60K_100K;
    }

    public void set_60K_100K(EIIncomePartyData _60K_100K) {
        this._60K_100K = _60K_100K;
    }

    public EIIncomePartyData get_100K_125K() {
        return _100K_125K;
    }

    public void set_100K_125K(EIIncomePartyData _100K_125K) {
        this._100K_125K = _100K_125K;
    }

    public EIIncomePartyData get_125K_150K() {
        return _125K_150K;
    }

    public void set_125K_150K(EIIncomePartyData _125K_150K) {
        this._125K_150K = _125K_150K;
    }

    public EIIncomePartyData get_150K_MORE() {
        return _150K_MORE;
    }

    public void set_150K_MORE(EIIncomePartyData _150K_MORE) {
        this._150K_MORE = _150K_MORE;
    }

}
