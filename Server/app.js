var   express = require("express");
var   app     = express();
var mongoose = require("mongoose");
mongoose.connect("mongodb://127.0.0.1:27017");
var Student = require("./models/student");

Student.create({
  rollNo      : "0201cd151024",
  email       : "dhrjsoni01@gmail.com",
  firstName   : "Dheeraj",
  middleName  : "Kumar",
  lastName    : "Soni",
  password    : "dheeraj",
  mobile      : "9589719689",
  branchCode  : "CS15",
  semester    : "S5",
},function(err,data){
  if(err){
    console.log(err);
  }else{
    console.log(data);
  }
});
