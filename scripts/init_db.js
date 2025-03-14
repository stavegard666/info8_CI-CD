#!/bin/bash


db = db.getSiblingDB('tinyX')

db.createCollection('users')
db.createCollection('posts')
db.createCollection('likes')
db.createCollection('follows')
db.createCollection('blocks')





