const port = process.env.PORT || 8080;


var   express     = require("express");
var   app         = express();
var   mongoose    = require("mongoose");
var   bodyParser  = require('body-parser');
var   morgan      = require('morgan');
var   jwt         = require('jsonwebtoken');
var   config      = require('./config');
var   passport    = require('passport')



const STUDENT     = require("./models/student");
const ADMIN       = require("./models/admin");
const ATTENDANCE  = require("./models/attendance");
const BRANCH      = require("./models/branch");
const BATCH       = require("./models/batch");
const FACULTY     = require("./models/faculty");
const apiRoutes   = require("./router");
const studentRoutes = require('./routes/studentsRoute');
const facultyRoute = require('./routes/facultyRoute');
const adminRoute = require('./routes/adminRoute');

app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());
app.use(passport.initialize());
require('./functions/loginStudent')(passport);


mongoose.connect(config.databaseLocal,function(err,done){
  console.log("db connection");
  if (err) {
    console.log("connection error" + err);

  }else {
    console.log("connected");
  }
});

app.set('superSecret', config.secret);

app.use(morgan('dev'));
app.use('/api',apiRoutes);
app.use('/api/student',studentRoutes);
app.use('/api/faculty',facultyRoute);
app.use('/api/admin',adminRoute);




app.listen(port,function(){
  console.log("app server is running on port "+ port);
})
