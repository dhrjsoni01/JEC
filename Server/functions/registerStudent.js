const student = require('../models/student');
var bcrypt = require('bcrypt');
const saltRounds = 10;
exports = function(req){
  if (!req.body.rollNo ||!req.body.email||!req.body.password||!req.body.semester||!req.body.branchCode) {
    res.send("somthing is missing,please send all details");
  }else {

    var newStudent = new student({
      rollNo      : req.body.rollNo,
      email       : req.body.email,
      // name : {
      //   first : String,
      //   middle: String,
      //   last  : String
      // },
      // mobile      : String,
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
}
