package me.Votifier.server.model.documents.BoxplotIncomeDocuments;
import org.springframework.data.mongodb.core.mapping.Field;
public class BoxplotEnsembleIncome {
    public BoxplotEnsembleIncome() {
        // No-args constructor
    }
    @Field("0_35K")
    private BoxplotIncomePlotData _0_35K;

    @Field("35K_60K")
    private BoxplotIncomePlotData _35K_60K;

    @Field("60K_100K")

    private BoxplotIncomePlotData _60K_100K;

    @Field("100K_125K")

    private BoxplotIncomePlotData _100K_125K;

    @Field("125K_150K")

    private BoxplotIncomePlotData _125K_150K;

    @Field("150K_MORE")

    private BoxplotIncomePlotData _150K_MORE;

    //getter and setter

    public BoxplotIncomePlotData get_0_35K() {
        return _0_35K;
    }

    public void set_0_35K(BoxplotIncomePlotData _0_35K) {
        this._0_35K = _0_35K;
    }

    public BoxplotIncomePlotData get_35K_60K() {
        return _35K_60K;
    }

    public void set_35K_60K(BoxplotIncomePlotData _35K_60K) {
        this._35K_60K = _35K_60K;
    }

    public BoxplotIncomePlotData get_60K_100K() {
        return _60K_100K;
    }

    public void set_60K_100K(BoxplotIncomePlotData _60K_100K) {
        this._60K_100K = _60K_100K;
    }

    public BoxplotIncomePlotData get_100K_125K() {
        return _100K_125K;
    }

    public void set_100K_125K(BoxplotIncomePlotData _100K_125K) {
        this._100K_125K = _100K_125K;
    }

    public BoxplotIncomePlotData get_125K_150K() {
        return _125K_150K;
    }

    public void set_125K_150K(BoxplotIncomePlotData _125K_150K) {
        this._125K_150K = _125K_150K;
    }
   
}
