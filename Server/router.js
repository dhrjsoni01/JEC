const auth = require('basic-auth');
const jwt = require('jsonwebtoken');
const express = require('express');
const router = express.Router();
const student = require('./models/student');
var bcrypt = require('bcrypt');
const saltRounds = 10;

router.use(function(req,res,next){
  console.log("middleware  from router.use in routerjs");
  next();
});

router.get('/',function(req,res){
  console.log("from root router in routerjs");
  res.send("from root route");
});
router.get('/auth',function(req,res){
  const credentials = auth(req);
  if (!credentials) {
    console.log("auth error");
  }else {
    console.log(credentials);
    res.send(credentials);
  }
});
router.post('/student/signup',function(req,res){
  if (!req.body.rollNo ||!req.body.email||!req.body.password||!req.body.semester||!req.body.branchCode) {
    res.send("somthing is missing,please send all details");
  }else  {
    var newStudent = new student({
      rollNo      : req.body.rollNo,
      email       : req.body.email,
      branchCode  : req.body.branchCode,
      semester    : req.body.semester,
    });
    bcrypt.hash(req.body.password, saltRounds, function(err, hash) {
      if (err) {
        console.log("error in hashing "+ err);
        return(err);
      }else {
        newStudent.password = hash;
        newStudent.save(function(err,studentNew){
          if (err) {
            console.log("error cant save student" + err);
            res.send("signup failed, unable to save new student");
          }else {
            console.log(studentNew);
            res.send("signup success");
          }
        });
      }
    });
  }
});

router.post('/studnet/login')






module.exports = router;
