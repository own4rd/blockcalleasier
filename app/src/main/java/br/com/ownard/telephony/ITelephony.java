package br.com.ownard.telephony;


public interface ITelephony {

    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}