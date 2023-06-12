package com.onpu.app.util

//import androidx.viewpager2.widget.ViewPager2
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.RequestOptions
//import com.bumptech.glide.request.target.Target
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
import com.onpu.domain.util.orFalse
import com.onpu.domain.util.tryOr
import kotlin.math.roundToInt


inline val Int.dpToPx get() = (this * (Resources.getSystem().displayMetrics.density + 0.25F)).roundToInt()
inline val Float.dpToPx get() = this * (Resources.getSystem().displayMetrics.density + 0.25F)
inline val Int.pxToDp get() = (this / (Resources.getSystem().displayMetrics.density + 0.25F)).roundToInt()
inline val Float.pxToDp get() = this / (Resources.getSystem().displayMetrics.density + 0.25F)

fun View.backgroundFromWindowBackground() {
    val  typeValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.windowBackground, typeValue, true)
    tryOr({ backgroundColor(typeValue.resourceId) }) { setBackgroundResource(typeValue.resourceId) }
}

fun View.setClickableAndFocusable(isClickable : Boolean){
    this.isClickable = isClickable
    this.isFocusable = isClickable
}
fun View.string(@StringRes stringId: Int) = context.string(stringId)
fun View.string(@StringRes stringId: Int, vararg formatArgs: Any?) = context.string(stringId, *formatArgs)

fun View.spanned(@StringRes stringId: Int) = string(stringId).asHtml()
fun View.spanned(@StringRes stringId: Int, vararg formatArgs: Any?) = string(stringId, *formatArgs).asHtml()

fun View.quantityString(@PluralsRes id: Int, quantity: Int) = context
    .quantityString(id, quantity)
fun View.quantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?) = context
    .quantityString(id, quantity, *formatArgs)

fun View.quantitySpanned(@PluralsRes id: Int, quantity: Int) = context
    .quantitySpanned(id, quantity)
fun View.quantitySpanned(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?) = context
    .quantitySpanned(id, quantity, *formatArgs)

fun View.padding(dp: Int) = padding(dp, dp, dp, dp)

fun View.padding(
    start: Int, top: Int, end: Int, bottom: Int
) = setPadding(start.dpToPx, top.dpToPx, end.dpToPx, bottom.dpToPx)

fun View.paddingStart(dp: Int) = padding(dp, paddingTop, paddingEnd, paddingBottom)
fun View.paddingTop(dp: Int) = padding(paddingStart, dp, paddingEnd, paddingBottom)
fun View.paddingEnd(dp: Int) = padding(paddingStart, paddingTop, dp, paddingBottom)
fun View.paddingBottom(dp: Int) = padding(paddingStart, paddingTop, paddingEnd, dp)

inline fun <T> View.onClickListener(crossinline onClickListener: () -> T?) = setOnClickListener { onClickListener() }

inline fun <T> View.onClickListener(
    isHideKeyboard: Boolean = false, crossinline onClickListener: () -> T?
) = setOnClickListener { if (isHideKeyboard) hideKeyboard(); onClickListener() }

fun View.backgroundTintList(@ColorRes resId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        backgroundTintList = context.colorStateList(resId)
    } else {
        background.setColorFilter(context.color(resId), PorterDuff.Mode.SRC_ATOP)
    }
}

fun View.backgroundColor(@ColorRes colorId: Int) = setBackgroundColor(context.color(colorId))

fun View.clearFocusAndHideKeyboard() {
    clearFocus()
    hideKeyboard()
}

fun View.hideKeyboard() = context
    .inputMethodManager
    .hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)


fun View.showKeyboard()  = context
    .inputMethodManager
    .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

fun View.backgroundTint(@ColorRes resId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        backgroundTintList = context.colorStateList(resId)
    } else {
        background.setColorFilter(context.color(resId), PorterDuff.Mode.SRC_ATOP)
    }
}

inline fun View.onLayoutWasDrawnListener(crossinline onLayoutWasDrawn: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            onLayoutWasDrawn.invoke(this@onLayoutWasDrawnListener)
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }

    })
}

inline fun View.onKeyboardVisibleListener(
    crossinline execute: (Boolean) -> Unit
) = findViewById<View>(android.R.id.content).let {
    it.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        it.getWindowVisibleDisplayFrame(r)
        val screenHeight = it.rootView.height
        val keypadHeight = screenHeight - r.bottom
        execute.invoke(keypadHeight > screenHeight * 0.15)
    }
}

var ViewGroup.isLocked: Boolean
    get() = !isEnabled
    set(value) {
        isEnabled = !value
        children.forEach {
            when (it) {
                is ViewGroup -> it.isLocked = value
                is ProgressBar -> it.isVisible = value
                is Button, is ImageButton -> it.isClickable = !value
                else -> it.isEnabled = !value
            }
        }
    }

inline fun ViewPager2.onPageSelectedListener(crossinline onPageSelected: (position: Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected.invoke(position)
        }
    })
}

fun ImageView.imageTintList(@ColorRes resId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        imageTintList = context.colorStateList(resId)
    } else {
        ImageViewCompat.setImageTintList(this, context.colorStateList(resId))
    }
}

fun TextView.setTextViewTextAsLink(text: String, onLinkClickListener : (link : String) -> Unit? = {}) {
    val spannableString = SpannableString(text)

    text.findEmailsInText().forEach {link ->
        val clickableSpan = object : ClickableSpan() { override fun onClick(widget: View) { onLinkClickListener.invoke(link) } }
        spannableString.setSpan(UnderlineSpan(), text.indexOf(link), text.indexOf(link) + link.length, 0)
        spannableString.setSpan(ForegroundColorSpan(this.currentTextColor), text.indexOf(link), text.indexOf(link) + link.length, 0)
        spannableString.setSpan(clickableSpan, text.indexOf(link), text.indexOf(link) + link.length, 0)
    }
    this.text = spannableString
}

//fun ImageView.load(model: Any?, error: Int = 0, size: Int = 0) = Glide
//    .with(this)
//    .load(model)
//    .apply { if (size != 0) RequestOptions().override(size) }
//    .error(error)
//    .into(this)
//    .view

//fun ImageView.loadCenterCrop(model: Any?, error: Int = 0) = Glide
//    .with(this)
//    .load(model)
//    .centerCrop()
//    .error(error)
//    .into(this)
//    .view

//fun ImageView.loadAnd(model: Any?, error: Int = 0, function: (Drawable) -> Unit) = Glide.with(this)
//    .load(model)
//    .centerCrop()
//    .listener(object : RequestListener<Drawable> {
//        override fun onLoadFailed(
//            e: GlideException?,
//            model: Any?,
//            target: Target<Drawable>?,
//            isFirstResource: Boolean
//        ): Boolean {
//            println(e?.message)
//            return false
//        }
//
//        override fun onResourceReady(
//            resource: Drawable?,
//            model: Any?,
//            target: Target<Drawable>?,
//            dataSource: DataSource?,
//            isFirstResource: Boolean
//        ): Boolean {
//            resource?.let { function(it) }
//            return false
//        }
//
//    })
//    .error(error)
//    .into(this)
//    .view
//
//fun ImageView.loadCenterCrop(model: Any?, error: Int = 0, loadCallback: ((Result<Drawable>) -> Unit)?) = Glide
//    .with(this)
//    .load(model)
//    .listener(object : RequestListener<Drawable> {
//        override fun onLoadFailed(
//            e: GlideException?,
//            model: Any?,
//            target: Target<Drawable>?,
//            isFirstResource: Boolean
//        ): Boolean {
//            if (e != null) {
//                loadCallback?.invoke(Result.failure(e))
//            }
//            return false
//        }
//
//        override fun onResourceReady(
//            resource: Drawable?,
//            model: Any?,
//            target: Target<Drawable>?,
//            dataSource: DataSource?,
//            isFirstResource: Boolean
//        ): Boolean {
//            if (resource != null) {
//                loadCallback?.invoke(Result.success(resource))
//            }
//            return false
//        }
//
//    })
//    .centerCrop()
//    .error(error)
//    .into(this)
//    .view
//
//fun ImageView.corners(){
//    clipToOutline = true
//    setBackgroundResource(R.drawable.bg_meeting_person)
//}
//
//fun ImageView.load(model: Any?, error: Int = 0, onResourceReady: () -> Unit) = Glide
//    .with(this)
//    .load(model)
//    .listener(
//        object : RequestListener<Drawable> {
//            override fun onLoadFailed(
//                e: GlideException?,
//                model: Any?,
//                target: Target<Drawable>?,
//                isFirstResource: Boolean
//            ) = false
//
//            override fun onResourceReady(
//                resource: Drawable?,
//                model: Any?,
//                target: Target<Drawable>?,
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//            ): Boolean {
//                onResourceReady()
//                return false
//            }
//        }
//    )
//    .error(error)
//    .into(this)
//    .view

//fun ImageView.loadWithRoundedCorners(
//    model: Any?,
//    @DimenRes radius: Int = android.R.dimen.round_corners_radius,
//    @DrawableRes placeholder: Int = android.R.color.transparent,
//    @DrawableRes error: Int = placeholder,
//    function: (isSuccess: Boolean) -> Unit = {}
//
//) {
//    val valueInPixels = context.resources.getDimension(radius)
//    var requestOptions = RequestOptions()
//    requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(valueInPixels.toInt()))
//    load(model, requestOptions, placeholder, error, function)
//}

fun TextView.drawables(
    @DrawableRes start: Int, @DrawableRes top: Int, @DrawableRes end: Int, @DrawableRes bottom: Int
) = setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)

fun TextView.drawablesStart(@DrawableRes resId: Int) = drawables(resId, 0, 0, 0)
fun TextView.drawablesTop(@DrawableRes resId: Int) = drawables(0, resId, 0, 0)
fun TextView.drawablesEnd(@DrawableRes resId: Int) = drawables(0, 0, resId, 0)
fun TextView.drawablesBottom(@DrawableRes resId: Int) = drawables(0, 0, 0, resId)

fun TextView.text(@StringRes stringId: Int) { text = string(stringId) }

fun TextView.text(@StringRes stringId: Int, vararg formatArgs: Any?) {
    text = string(stringId, *formatArgs)
}

fun TextView.textFromHtml(@StringRes stringId: Int, vararg formatArgs: Any?) {
    text = context.getString(stringId, *formatArgs).asHtml()
}

fun TextView.textColor(@ColorRes colorId: Int) = setTextColor(context.color(colorId))

fun TextInputLayout.createRequiredField(editText: EditText, @StringRes resId: Int) = editText
    .addTextChangedListener {
        error = if (it.isNullOrEmpty()) string(resId) else null
        isErrorEnabled = it.isNullOrEmpty()
    }

val EditText.requestFocus: Unit get() { requestFocus(); showKeyboard() }

val EditText.clearFocus: Unit get() { clearFocus(); hideKeyboard() }


var EditText.string: String
    get() = text.toString()
    set(value) { if (value.isNotEmpty()) text = SpannableStringBuilder(value) else text.clear() }

inline fun EditText.onTextChangedListener(
    crossinline beforeTextChanged: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTextChanged: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline afterTextChanged: (text: String) -> Unit = {}
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = afterTextChanged.invoke(s.toString())

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(text, start, count, after)
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(text, start, before, count)
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}

inline fun EditText.onAfterTextChangedListener(
    crossinline afterTextChanged: (text: String) -> Unit = {}
) = onTextChangedListener(afterTextChanged = afterTextChanged)

inline fun <T> EditText.onDoneClickListener(
    crossinline onDoneClick: (text: String) -> T?
) = onActionClickListener(EditorInfo.IME_ACTION_DONE, onDoneClick)

inline fun <T> EditText.onDoneOrNextClickListener(
    crossinline onDoneClick: (text: String) -> T?
) = setOnEditorActionListener { _, actionId, _ ->
    if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_NEXT == actionId) {
        clearFocus()
        hideKeyboard()
        onDoneClick.invoke(string)
        return@setOnEditorActionListener true
    }
    false
}

inline fun <T> EditText.onSearchClickListener(
    crossinline onDoneClick: (text: String) -> T?
) = onActionClickListener(EditorInfo.IME_ACTION_SEARCH, onDoneClick)

inline fun <T> EditText.onActionClickListener(
    action: Int, crossinline onDoneClick: (text: String) -> T?
) = setOnEditorActionListener { _, actionId, _ ->
    println("dlanwdlkjandjlk 1 actionId $actionId action $action" )
    if (actionId == action) {
    println("dlanwdlkjandjlk 2")
        clearFocus()
        hideKeyboard()
        onDoneClick.invoke(string)
        return@setOnEditorActionListener true
    }
    println("dlanwdlkjandjlk 3")
    false
}

//inline fun ViewPager2.onPageSelectedListener(crossinline onPageSelected: (position: Int) -> Unit) {
//    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//            onPageSelected.invoke(position)
//        }
//
//    })
//}

fun WebView.evaluateJSFile(
    fileName: String, callback: (String) -> Unit
) = evaluateJavascript(context.assetsReadText(fileName)) { callback("$fileName -> error: $it") }

fun WebView.evaluateJavascript(script: String) = evaluateJavascript(script) {  }

inline val ListPopupWindow.isShow get() = listView?.isShown.orFalse

fun AlertDialog.Builder.setTitle(
    @StringRes stringId: Int, vararg formatArgs: Any?
): AlertDialog.Builder = setTitle(context.string(stringId, *formatArgs))

fun AlertDialog.Builder.setMessage(
    @StringRes stringId: Int, vararg formatArgs: Any?
): AlertDialog.Builder = setMessage(context.string(stringId, *formatArgs))

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun NavController.navigateOrPopUpTo(@IdRes destinationId: Int) {
    try {
        getBackStackEntry(destinationId)
        navigate(destinationId, null, NavOptions.Builder().apply {
            setPopUpTo(destinationId, true)
        }.build())
    } catch (e: Exception) {
        navigate(destinationId)
    }
}

fun NavController.safeNavigate(@IdRes currentDestinationId: Int, @IdRes resId: Int) {
    if (currentDestination == findDestination(currentDestinationId)) {
        navigate(resId)
    }
}

fun NavController.safeNavigate(@IdRes currentDestinationId: Int, @IdRes resId: Int, args: Bundle?) {
    if (currentDestination == findDestination(currentDestinationId)) {
        navigate(resId, args)
    }
}

//fun TextView.setUserInfo(name: String, age: Int, sex: Sex) {
//    text = context.getString(R.string.user_name_age, name, age)
//
//    drawablesEnd(
//        when (sex) {
//            Sex.MALE -> R.drawable.ic_male_24dp
//            Sex.FEMALE -> R.drawable.ic_female_24dp
////            Sex.TRANS -> R.drawable.ic_rainbow
//            Sex.UNKNOWN -> 0
//        }
//    )
//}
//
//fun TextView.setPurpose(purpose: Purpose) {
//    text(
//        when (purpose) {
//            Purpose.FIND_FRIEND -> R.string.purpose_find_friend
//            Purpose.STRONG_RELATIONSHIP -> R.string.purpose_strong_relationship
//            Purpose.NOT_SPECIFIED -> R.string.purpose_not_specified
//        }
//    )
//}
//
//fun TextView.setInviteTime(time: String, sex: Sex, isFinishing: Boolean) {
//    text(
//        if (sex == Sex.FEMALE) R.string.time_to_invite_female
//        else R.string.time_to_invite_male,
//        time
//    )
//
//    setBackgroundResource(
//        if (isFinishing) R.drawable.matches_red_time_to_invite
//        else R.drawable.matches_green_time_to_invite
//    )
//
//    val textColor = ContextCompat.getColor(
//        context,
//        if (isFinishing) R.color.matches_red_time
//        else R.color.matches_green_time
//    )
//    setTextColor(textColor)
//}