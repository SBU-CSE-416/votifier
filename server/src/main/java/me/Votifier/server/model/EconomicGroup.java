package me.Votifier.server.model;

public enum EconomicGroup {
    _0_35K ("0_35K"),
    _35K_60K("35K_60K"),
    _60K_100K("60K_100K"),
    _100K_125K("100K_125K"),
    _125K_150K("125K_150K"),
    _150K_MORE("150K_MORE");

    private final String group;

    EconomicGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }    
}
