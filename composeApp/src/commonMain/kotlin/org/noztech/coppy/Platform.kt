package org.noztech.coppy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform