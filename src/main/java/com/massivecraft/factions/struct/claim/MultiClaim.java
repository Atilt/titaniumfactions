package com.massivecraft.factions.struct.claim;

import java.util.List;

public interface MultiClaim extends Claim {

    void appendSuccess(String success);

    void appendFailure(String failure);

    int getSuccessSize();

    int getFailureSize();

    List<String> getSuccesses();

    List<String> getFailures();
}
