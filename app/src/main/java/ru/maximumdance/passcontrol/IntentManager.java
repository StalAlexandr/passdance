package ru.maximumdance.passcontrol;

import android.content.Context;
import android.content.Intent;

public class IntentManager {

    Context context;

    public IntentManager(Context context) {
        this.context = context;
    }

    public Intent onPerson() {
        return new Intent(context, PersonActivity.class);
    }

    public Intent onPersonSearch() {
        return new Intent(context, PersonSearchActivity.class);
    }


    public Intent onPersonHistory() {
        return new Intent(context, PersonHistoryActivity.class);
    }

    public Intent onPass() {
        return new Intent(context, PassActivity.class);
    }



}
