var mongoose = require("mongoose");
var subjectSchema = new mongoose.Schema({
  subjectCode   : String,
  subjectName   : String,
  department    : String,
});

module.exports = mongoose.model("Subject", subjectSchema);
