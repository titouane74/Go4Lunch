package com.fleb.go4lunch.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fleb.go4lunch.repository.ChoiceRepository;

/**
 * Created by Florence LE BOURNOT on 06/11/2020
 */
public class RestaurantChoiceWorker extends Worker {

    public static final String TAG = "TAG_RESTOCH";
    private ChoiceRepository mChoiceRepo;

    public RestaurantChoiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mChoiceRepo = new ChoiceRepository();
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {

        mChoiceRepo.removePreviousChoice();

        return ListenableWorker.Result.success();
    }
}
