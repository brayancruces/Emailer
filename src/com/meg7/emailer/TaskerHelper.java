/*
 * Copyright 2014 Mostafa Gazar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meg7.emailer;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.meg7.emailer.service.TaskerService;
import com.meg7.emailer.util.MLog;
import com.meg7.emailer.util.Scheduler;

/**
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class TaskerHelper {

    private static final String TAG = TaskerHelper.class.getSimpleName();

    private static final String PREF_IS_RUNNING = "isTaskerRunning";

    public static void startTasker(Context context) {
        if (context == null) {
            MLog.w(TAG, "Couldn't start service, context is null");
            return;
        }

        EmailerManager.resetProgress(context);

        final Intent i = new Intent(context, TaskerService.class);
        context.startService(i);

        // XXX Would not it make more sense to move this inside the service where the actual magic happens.
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_RUNNING, true)
                .commit();
    }

    public static void cancelFutureTasks(Context context) {
        if (context == null) {
            MLog.w(TAG, "Failed to stop future tasks, context is null");
            return;
        }

        EmailerManager.resetProgress(context);

        Scheduler.cancelScheduledWakes(context);

        // XXX :: We did not really stop current cycle so that might be running.
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_RUNNING, false)
                .commit();
    }

    public static boolean isTaskerRunning(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_IS_RUNNING, false);
    }

}
