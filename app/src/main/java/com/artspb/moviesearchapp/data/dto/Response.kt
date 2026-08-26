package com.artspb.moviesearchapp.data.dto

// Родительский класс ответа от сервера. От него наследуются все DTO-ответы в Data-слое.
// Даже если запрос всего один, этот базовый класс позволяет слою Domain единообразно получать
// результат и статус выполнения через resultCode, как мы учим на курсе по Clean Architecture.
open class Response() {
    var resultCode = 0
}
