package com.hyflip.mod.errors

class CommandError(message: String, cause: Throwable) : Error(message, cause)