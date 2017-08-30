var mongoose = require("mongoose");

var studentSchema = mongoose.Schema({

    rollNo      : {type: String, unique:true },
    email       : {type: String, unique:true },
    name : {
      first : String,
      middle: String,
      last  : String
    },
    password    : String,
    mobile      : String,
    branchCode  : String,
    semester    : String,
    record      : [
                      {
                          name        : String,
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      }
                  ]
});


module.exports = mongoose.model("Student", studentSchema);
