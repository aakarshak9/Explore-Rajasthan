package com.example.aakarshak.explore.utils;

import android.content.Context;

import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.local.ResourceClass;

public class UtilityInjector {

    /**
     * Private Constructor to avoid direct instantiation of {@link UtilityInjector}
     */
    private UtilityInjector() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

    /**
     * Method that provides/injects the {@link ResourceClass} instance which
     * deals with the {@link android.content.res.Resources}
     *
     * @param context A {@link Context} to derive the {@link android.content.res.Resources} instance
     * @return Instance of {@link ResourceClass}
     */
    private static ResourceClass provideResourceRepository(Context context) {
        return ResourceClass.getInstance(context.getPackageName(), context.getResources(), Execute.getInstance());
    }

    /**
     * Method that provides/injects the {@link AppClassRepo} instance which
     * interfaces with {@link ResourceClass}
     *
     * @param context A {@link Context} to derive the {@link android.content.res.Resources} instance
     * @return Instance of {@link AppClassRepo}
     */
    public static AppClassRepo provideAppRepository(Context context) {
        return AppClassRepo.getInstance(provideResourceRepository(context));
    }
}
