package com.cloudchewie.auth.legacy

/**
 * The saved tokens to storage
 */
data class SavedTokens(val tokens: List<Token>, val tokenOrder: List<String>)
