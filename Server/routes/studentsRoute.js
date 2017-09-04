const express = require('express');
const router = express.Router();
const student = require('../models/student');
var bcrypt = require('bcrypt');
const saltRounds = 10;

router.use(function(req,res,next){
  console.log("middleware  for student request validation in routerjs");
  next();
});

router.get('/attendacne',function(req,res){
  res.send("student atttendance");
});
router.get('/noti',function(req,res){
  res.send("student notification")
});
router.get('/profile',function(req,res){
  res.send("student profile");
});
router.post('/profile/update',function(req,res){
  res.send("student profile update");
});

module.exports = router;
