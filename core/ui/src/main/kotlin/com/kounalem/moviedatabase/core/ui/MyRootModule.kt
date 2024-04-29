package com.kounalem.moviedatabase.core.ui

import android.content.Context
import android.content.Intent
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.airbnb.android.showkase.models.Showkase

@ShowkaseRoot
class MyRootModule : ShowkaseRootModule

fun showShowkase(context: Context, onShowkaseMissing: () -> Unit) {
    val intent = Showkase.getBrowserIntent(context)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}
