package com.fleb.go4lunch.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.ChoiceRepository;
import com.fleb.go4lunch.repository.RestaurantRepository;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;

/**
 * Created by Florence LE BOURNOT on 06/11/2020
 */
public class RestaurantChoiceWorker extends Worker {

    public static final String TAG = "TAG_RESTOCH";
    private ChoiceRepository mChoiceRepo;

    public RestaurantChoiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mChoiceRepo = new ChoiceRepository();
        Log.d(TAG, "RestoChoiceWorker: ");
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        Log.d(TAG, "doWork: ");

        mChoiceRepo.removePreviousChoice();

        return ListenableWorker.Result.success();
    }
}
