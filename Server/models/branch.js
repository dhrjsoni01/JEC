var mongoose = require("mongoose");
var branchSchema = new mongoose.Schema({
  name        : String,
  branchCode  : {type: String,unique: true},
  student     : [
    {
      type: mongoose.Schema.Types.ObjectId,
      rel : "Student"
    }
  ]
});

module.exports =  mongoose.model("Branch",branchSchema);
