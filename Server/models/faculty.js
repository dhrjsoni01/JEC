var mongoose = require("mongoose");
var facultySchema = new mongoose.Schema({
  name : {
    first : String,
    middle: String,
    last  : String
  },
  gender : String,
  department : String,
  password   : String,
  email      : String,
  mobile     : String,

});

module.exports = mongoose.model("Faculty", facultySchema);
