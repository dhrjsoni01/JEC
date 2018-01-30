var mongoose = require("mongoose");
var batchSchema = new mongoose.Schema({
  name: String,
  branch: [{
      name        : String,
      branchCode  : String,
      student     : [
        {
          type: mongoose.Schema.Types.ObjectId,
          rel : "Student"
        }
      ]
  }]
});

module.exports =  mongoose.model("Batch",batchSchema);
