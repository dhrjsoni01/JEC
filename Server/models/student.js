var mongoose = require("mongoose");

var studentSchema = new mongoose.Schema({

    rollNo      : {type: String, unique:true },
    email       : {type: String, unique:true },
    firstName   : String,
    middleName  : String,
    lastName    : String,
    password    : String,
    mobile      : String,
    branchCode  : String,
    semester    : String,
    record      : [
                      {
                          name        : {type: String, default:"S1"},
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      },
                      {
                          name        : {type: String, default:"S2"},
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      },
                      {
                          name        : {type: String, default:"S3"},
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      },
                      {
                          name        : {type: String, default:"S4"},
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      },
                      {
                          name        : {type: String, default:"S5"},
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      },
                      {
                          name        : {type: String, default:"S6"},
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      },
                      {
                          name        : {type: String, default:"S7"},
                          startDate   : String,
                          endDate     : String,
                          data        : [
                            {
                                type    : mongoose.Schema.Types.ObjectId,
                                ref     : "Attendance"
                            }
                          ]
                      },
                      {
                          name        : {type: String, default:"S8"},
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
