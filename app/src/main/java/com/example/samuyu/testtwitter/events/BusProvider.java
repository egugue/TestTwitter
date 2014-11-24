package com.example.samuyu.testtwitter.events;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusProvider {
    private static final String TAG = BusProvider.class.getSimpleName();
    //private static Bus mBus = new Bus();
    private static Bus mBus = new Bus(ThreadEnforcer.ANY);

    private BusProvider() {
        //no instances
    }

    public static Bus getInstance() {
        return mBus;
    }
}
