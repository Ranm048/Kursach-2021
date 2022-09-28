package com.example.myapplication

class Message {
    var message: String? = null
    var m: String? = null
    var senderId: String? = null
    var signature: Boolean? = false

    constructor(){}

    constructor(message : String?, senderId: String?){
        this.message = message
        this.senderId = senderId
    }
    constructor(message : String?, senderId: String?, signature: Boolean?){
        this.message = message
        this.senderId = senderId
        this.signature = signature
    }
    constructor(message : String?, senderId: String?, m : String?, signature: Boolean?){
        this.message = message
        this.senderId = senderId
        this.m = m
        this.signature = signature
    }
}