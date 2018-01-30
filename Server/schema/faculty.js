var mongoose = require("mongoose");
var facultySchema = new mongoose.Schema({
  name : {
    first : String,
    middle: String,
    last  : String
  },
  gender : {type:String, default:"M"},
  department : String,
  password   : {type: String,required: true},
  email      : {type: String, unique:true, required:true },
  mobile     : String,
  verified   :{type: Boolean, default:false}

});

module.exports = mongoose.model("Faculty", facultySchema);
