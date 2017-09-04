const batchData = {
  name: "2015",
};
const adminData = {
  name : {
    first : "Dheeraj",
    middle: "kumar",
    last  : "soni"
  },
  gender : "male",
  email      : "9589719689",
  mobile     : "9589719689",
  type       : "developer"
};

const facultyData = {
  name : {
    first : "Dheeraj",
    middle: "kumar",
    last  : "soni"
  },
  department : "cse",
  gender : "male",
  email      : "9589719689",
  mobile     : "9589719689",
};

const branchData= {
  name        : "Computer Science",
  branchCode  : "CS2015",
};

BRANCH.create(branchData,function(err,data){
  if(err){
    console.log(err);
  }else{
    console.log(data);
  }
});

STUDENT.create({
  rollNo      : "0201cd151024",
  email       : "dhrjsoni01@gmail.com",
  name : {
    first : "Dheeraj",
    middle: "kumar",
    last  : "soni"
  },
  password    : "dheeraj",
  mobile      : "9589719689",
  branchCode  : "CS15",
  semester    : "S5",
},function(err,data){
  if(err){
    console.log(err);
  }else{
    console.log(data);
  }
});

BATCH.create(batchData,function(err,data){
  if(err){
    console.log(err);
  }else{
    console.log(data);
  }
});

FACULTY.create(facultyData,function(err,data){
  if(err){
    console.log(err);
  }else{
    console.log(data);
  }
})

ADMIN.create(adminData,function(err,data){
  if(err){
    console.log(err);
  }else{
    console.log(data);
  }
});
