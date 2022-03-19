package com.learnpa.models

import kotlinx.serialization.Serializable

@Serializable
data class Repository (
  var id                 : Int = 0,
  val author             : String?            = null,
  val name               : String?            = null,
  val avatar             : String?            = null,
  val description        : String?            = null,
  val url                : String?            = null,
  val language           : String?            = null,
  val languageColor      : String?            = null,
  val stars              : Int?               = null,
  val forks              : Int?               = null,
  val currentPeriodStars : Int?               = null,
  val builtBy            : ArrayList<BuiltBy> = arrayListOf(),
)

@Serializable
data class BuiltBy (

  val username : String? = null,
  val href     : String? = null,
  val avatar   : String? = null

)