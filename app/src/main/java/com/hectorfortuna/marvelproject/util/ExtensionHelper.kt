package com.hectorfortuna.marvelproject.util

import android.content.Context
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setError(context: Context, stringResId: Int?){
    error = if(stringResId != null){
        context.getString(stringResId)
    } else null
}
