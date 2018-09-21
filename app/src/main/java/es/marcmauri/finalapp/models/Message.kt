package es.marcmauri.finalapp.models

import java.util.*

data class Message(val authorId: String, val message: String, val profileImageURL: String, val sentAt: Date)