package com.learnpea.models

data class Command(
    val action: Action,
    val target: Any?=null
)

enum class Action() {
    GO_TO_PAGE,
    LOADING,
    OFFLINE,
    SUCCESS,
    VALIDATING,
    FAILED,
    ONLINE,
    RETRYING,
    DONE,
}