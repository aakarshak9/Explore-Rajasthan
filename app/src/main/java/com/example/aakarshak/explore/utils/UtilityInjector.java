package com.example.aakarshak.explore.utils;

import android.content.Context;

import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.local.ResourceClass;

public class UtilityInjector {

    private UtilityInjector() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

    private static ResourceClass provideResourceRepository(Context context) {
        return ResourceClass.getInstance(context.getPackageName(), context.getResources(), Execute.getInstance());
    }

    public static AppClassRepo provideAppRepository(Context context) {
        return AppClassRepo.getInstance(provideResourceRepository(context));
    }
}
