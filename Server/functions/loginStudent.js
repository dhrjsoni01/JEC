var JwtStrategy = require('passport-jwt').Strategy,
    ExtractJwt = require('passport-jwt').ExtractJwt,
    config      = require("../config"),
    student     = require("../models/student"),
    admin       = require("../models/admin"),
    faculty     = require("../models/faculty");

module.exports = function(passport){
  var opts = {}
  opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
  opts.secretOrKey = 'config.secret';
  // opts.issuer = 'accounts.examplesoft.com';
  // opts.audience = 'yoursite.net';
  passport.use(new JwtStrategy(opts, function(jwt_payload, done) {
    console.log("#### inside passport.use in ***loginStudnt");
    student.findOne({rollNo:jwt_payload.rollNo}, function(err, student) {
        if (err) {
            return done(err, false);
        }
        if (student) {
            return done(null, student);
        } else {
            return done(null, false);
            // or you could create a new account
        }
    });
}));
};
