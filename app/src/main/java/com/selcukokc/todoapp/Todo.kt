package com.selcukokc.todoapp

import java.io.Serializable

data class Todo(var title: String, var description: String,var id: String):Serializable{}