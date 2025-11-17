package org.noztech.coppy.core.util

actual fun ShareText(text: String) {
    // Typically you’d open a UIActivityViewController, but that requires a UIViewController reference.
    // You can pass a callback up to your Swift side if you want full control.
    println("Sharing not implemented directly in Compose yet — use platform callback.")
}