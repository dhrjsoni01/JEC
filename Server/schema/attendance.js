var mongoose = require("mongoose");
var attendanceSchema = new mongoose.Schema({
  date        : Date,
  subject     : String,
  subjectCode : String,
  branchCode  : String,
  facultyID   :{type: mongoose.Schema.Types.ObjectId, rel : "Faculty"},
  FacultyName : String,
  present     :[ {rollNo : String}],
  absent      :[ {rollNo : String}]
});

module.exports = mongoose.model("Attendance", attendanceSchema);
