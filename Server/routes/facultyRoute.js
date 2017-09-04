const express = require('express');
const router = express.Router();
const student = require('../models/student');
const faculty = require('../models/faculty');

var bcrypt = require('bcrypt');
const saltRounds = 10;

router.use(function(req,res,next){
  console.log("middleware  for faculty request validation in routerjs");
  next();
});

router.get("/",function(req,res){
  res.send("result from faculty route");
})


module.exports = router;
