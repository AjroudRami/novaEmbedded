package com.subutai.nova.arduino;

public interface CommandCallback {

    void onSuccess(CommandResponse response);
    void onFailure(FailureResponse response);
}
