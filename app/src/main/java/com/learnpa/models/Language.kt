package com.learnpa.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class Language (

  @EncodeDefault val urlParam : String? = null,
  @EncodeDefault val name     : String? = null

)