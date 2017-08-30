var mongoose = require("mongoose");
var adminSchema = new mongoose.Schema({
  name : {
    first : String,
    middle: String,
    last  : String
  },
  gender : String,
  password   : String,
  email      : String,
  mobile     : String,
  type       : String
});

module.exports = mongoose.model("Admin", adminSchema);
