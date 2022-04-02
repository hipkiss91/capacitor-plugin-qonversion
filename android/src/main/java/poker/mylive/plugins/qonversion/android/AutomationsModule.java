package poker.mylive.plugins.qonversion.android;

/*
  Capacitor Android Library
 */
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/*
  Qonversion Android library
 */
import com.qonversion.android.sdk.automations.Automations;
import com.qonversion.android.sdk.automations.AutomationsDelegate;
import com.qonversion.android.sdk.automations.QActionResult;

import org.json.JSONArray;
import org.jetbrains.annotations.NotNull;

import android.app.Activity;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.fragment.app.FragmentActivity;

class AutomationsModule {
    private static final String EVENT_SCREEN_SHOWN = "automations_screen_shown";
    private static final String EVENT_ACTION_STARTED = "automations_action_started";
    private static final String EVENT_ACTION_FAILED = "automations_action_failed";
    private static final String EVENT_ACTION_FINISHED = "automations_action_finished";
    private static final String EVENT_AUTOMATIONS_FINISHED = "automations_finished";

    private AutomationsDelegate automationsDelegate = null;

    @PluginMethod
    public void subscribe(PluginCall call) {
        automationsDelegate = createAutomationsDelegate();
        Automations.setDelegate(automationsDelegate);
    }

    private AutomationsDelegate createAutomationsDelegate() {
        return new AutomationsDelegate() {
            @Override
            public void automationsDidShowScreen(@NotNull String screenId) {
                // eventEmitter.emit(EVENT_SCREEN_SHOWN, screenId);
            }

            @Override
            public void automationsDidStartExecuting(@NotNull QActionResult actionResult) {
                JSObject payload = EntitiesConverter.mapActionResult(actionResult);
                // eventEmitter.emit(EVENT_ACTION_STARTED, payload);
            }

            @Override
            public void automationsDidFailExecuting(@NotNull QActionResult actionResult) {
                JSObject payload = EntitiesConverter.mapActionResult(actionResult);
                // eventEmitter.emit(EVENT_ACTION_FAILED, payload);
            }

            @Override
            public void automationsDidFinishExecuting(@NotNull QActionResult actionResult) {
                JSObject payload = EntitiesConverter.mapActionResult(actionResult);
                // eventEmitter.emit(EVENT_ACTION_FINISHED, payload);
            }

            @Override
            public void automationsFinished() {
                // eventEmitter.emit(EVENT_AUTOMATIONS_FINISHED, null);
            }
        };
    }
}
