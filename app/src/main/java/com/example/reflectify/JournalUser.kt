package com.example.reflectify

import android.app.Application

class JournalUser: Application() {
    var username: String?= null
    var userId:String?=null
    companion object{
        var instance: JournalUser? = null
            get(){
                if(field==null){
                    //create a new instane
                    field = JournalUser()

                }
                return field
            }
        private set
    }
}