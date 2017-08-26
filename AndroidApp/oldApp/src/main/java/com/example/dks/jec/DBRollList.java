package com.example.dks.jec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DKS on 3/25/2017.
 */

public class DBRollList extends SQLiteOpenHelper {

    private static final String dbname = "students.db";
    private static final String id = "ID";
    private static final String roll = "rollno";
    private static final String name = "name";
    private static final String table_cross = "maintable";
    private static final String zero = "A1", one="A2",two="A3",three = "A4";
    private static final String tableSubject = "subjecttable";
    private static final String tableRecord = "recordtable";


    public DBRollList(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+table_cross);
        db.execSQL("create table "+table_cross+" ("+id+" INTEGER PRIMARY KEY AUTOINCREMENT, `"+zero+"` TEXT, `"+one+"` TEXT, `"+two+"` TEXT, `"+three+"` TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void getTableOfRoll(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS `"+table_name+"`");
        db.execSQL("create table `"+table_name+"` ("+id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+roll+" TEXT, "+name+" TEXT)");
    }

    public Boolean insertRoll(String roll,String name,String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.roll,roll);
        contentValues.put(this.name,name);
        long result = db.insert(table_name,null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getStudent(int id,String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `"+roll+"` , `"+name+"` from `"+table_name +"` where `"+this.id+"` like '"+id+"' ",null);
        return res;
    }

    public int strength(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `"+roll+"` , `"+name+"` from `"+table_name +"` ",null);
        return res.getCount();
    }

   /* public void clearRoll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM `"+table_name+"`");
    }
    */



    public void getcrosstable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+table_cross);
        db.execSQL("create table `"+table_cross+"` (`"+id+"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+zero+"` TEXT, `"+one+"` TEXT, `"+two+"` TEXT, `"+three+"` TEXT )");
    }

    public Boolean addDataCrossTable(String zero,String one,String two,String three){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.zero,zero);
        contentValues.put(this.one,one);
        contentValues.put(this.two,two);
        contentValues.put(this.three,three);
        long result = db.insert(table_cross,null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor getTableName(String year,int branch){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `"+year+"`  from `"+table_cross+"` where `"+this.id+"` like '"+branch+"' ",null);
        return res;
    }





    public void creatSubTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS `"+tableSubject+"`");
        db.execSQL("create table `"+tableSubject+"` (`"+id+"` INTEGER PRIMARY KEY AUTOINCREMENT, `S0` TEXT, `S1` TEXT, `S2` TEXT, `S3` TEXT, `S4` TEXT, `S5` TEXT, `S6` TEXT, `S7` TEXT, `S8` TEXT, `S9` TEXT, `S10` TEXT, `S11` TEXT, `S12` TEXT, `S13` TEXT, `S14` TEXT, `S15` TEXT, `S16` TEXT, `S17` TEXT, `S18` TEXT, `S19` TEXT, `S20` TEXT, `S21` TEXT, `S22` TEXT, `S23` TEXT, `S24` TEXT, `S25` TEXT, `S26` TEXT, `S27` TEXT)");
    }

    public Boolean insertSubject(String s0,String s1,String s2,String s3,String s4,String s5,String s6,String s7,String s8,String s9,String s10,String s11,String s12,String s13,String s14,String s15,String s16,String s17,String s18,String s19,String s20,String s21,String s22,String s23,String s24,String s25,String s26,String s27){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("S0",s0);
        contentValues.put("S1",s1);
        contentValues.put("S2",s2);
        contentValues.put("S3",s3);
        contentValues.put("S4",s4);
        contentValues.put("S5",s5);
        contentValues.put("S6",s6);
        contentValues.put("S7",s7);
        contentValues.put("S8",s8);
        contentValues.put("S9",s9);
        contentValues.put("S10",s10);
        contentValues.put("S11",s11);
        contentValues.put("S12",s12);
        contentValues.put("S13",s13);
        contentValues.put("S14",s14);
        contentValues.put("S15",s15);
        contentValues.put("S16",s16);
        contentValues.put("S17",s17);
        contentValues.put("S18",s18);
        contentValues.put("S19",s19);
        contentValues.put("S20",s20);
        contentValues.put("S21",s21);
        contentValues.put("S22",s22);
        contentValues.put("S23",s23);
        contentValues.put("S24",s24);
        contentValues.put("S25",s25);
        contentValues.put("S26",s26);
        contentValues.put("S27",s27);
        long result = db.insert(tableSubject,null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getSubjectName(String column,int row){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `"+column+"`  from `"+tableSubject+"` where `"+this.id+"` like '"+row+"' ",null);
        return res;
    }

    public Cursor getAllSubject(String column){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `"+column+"`, `id`  from `"+tableSubject+"` ",null);
        return res;
    }




    public void createRecordTable(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS "+tableRecord+"" +
                " (`"+id+"` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `date` DATE DEFAULT CURRENT_DATE," +
                " `datetime` DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                " `sync_status` INTEGER DEFAULT 0," +
                " `user` TEXT," +
                " `tablename` TEXT," +
                " `subject` INTEGER DEFAULT 99," +
                " `branch` INTEGER DEFAULT 9," +
                " `year` INTEGER DEFAULT 9," +
                "`sem` INTEGER DEFAULT 0, " +
                "`S1` INTEGER DEFAULT 9, `S2` INTEGER DEFAULT 9, `S3` INTEGER DEFAULT 9, `S4` INTEGER DEFAULT 9, `S5` INTEGER DEFAULT 9, " +
                "`S6` INTEGER DEFAULT 9, `S7` INTEGER DEFAULT 9, `S8` INTEGER DEFAULT 9, `S9` INTEGER DEFAULT 9, `S10` INTEGER DEFAULT 9, " +
                "`S11` INTEGER DEFAULT 9, `S12` INTEGER DEFAULT 9, `S13` INTEGER DEFAULT 9, `S14` INTEGER DEFAULT 9, `S15` INTEGER DEFAULT 9, " +
                "`S16` INTEGER DEFAULT 9, `S17` INTEGER DEFAULT 9, `S18` INTEGER DEFAULT 9, `S19` INTEGER DEFAULT 9, `S20` INTEGER DEFAULT 9, " +
                "`S21` INTEGER DEFAULT 9, `S22` INTEGER DEFAULT 9, `S23` INTEGER DEFAULT 9, `S24` INTEGER DEFAULT 9, `S25` INTEGER DEFAULT 9, " +
                "`S26` INTEGER DEFAULT 9, `S27` INTEGER DEFAULT 9, `S28` INTEGER DEFAULT 9, `S29` INTEGER DEFAULT 9, `S30` INTEGER DEFAULT 9, " +
                "`S31` INTEGER DEFAULT 9, `S32` INTEGER DEFAULT 9, `S33` INTEGER DEFAULT 9, `S34` INTEGER DEFAULT 9, `S35` INTEGER DEFAULT 9, " +
                "`S36` INTEGER DEFAULT 9, `S37` INTEGER DEFAULT 9, `S38` INTEGER DEFAULT 9, `S39` INTEGER DEFAULT 9, `S40` INTEGER DEFAULT 9, " +
                "`S41` INTEGER DEFAULT 9, `S42` INTEGER DEFAULT 9, `S43` INTEGER DEFAULT 9, `S44` INTEGER DEFAULT 9, `S45` INTEGER DEFAULT 9, " +
                "`S46` INTEGER DEFAULT 9, `S47` INTEGER DEFAULT 9, `S48` INTEGER DEFAULT 9, `S49` INTEGER DEFAULT 9, `S50` INTEGER DEFAULT 9, " +
                "`S51` INTEGER DEFAULT 9, `S52` INTEGER DEFAULT 9, `S53` INTEGER DEFAULT 9, `S54` INTEGER DEFAULT 9, `S55` INTEGER DEFAULT 9, " +
                "`S56` INTEGER DEFAULT 9, `S57` INTEGER DEFAULT 9, `S58` INTEGER DEFAULT 9, `S59` INTEGER DEFAULT 9, `S60` INTEGER DEFAULT 9, " +
                "`S61` INTEGER DEFAULT 9, `S62` INTEGER DEFAULT 9, `S63` INTEGER DEFAULT 9, `S64` INTEGER DEFAULT 9, `S65` INTEGER DEFAULT 9, " +
                "`S66` INTEGER DEFAULT 9, `S67` INTEGER DEFAULT 9, `S68` INTEGER DEFAULT 9, `S69` INTEGER DEFAULT 9, `S70` INTEGER DEFAULT 9, " +
                "`S71` INTEGER DEFAULT 9, `S72` INTEGER DEFAULT 9, `S73` INTEGER DEFAULT 9, `S74` INTEGER DEFAULT 9, `S75` INTEGER DEFAULT 9, " +
                "`S76` INTEGER DEFAULT 9, `S77` INTEGER DEFAULT 9, `S78` INTEGER DEFAULT 9, `S79` INTEGER DEFAULT 9, `S80` INTEGER DEFAULT 9, " +
                "`S81` INTEGER DEFAULT 9, `S82` INTEGER DEFAULT 9, `S83` INTEGER DEFAULT 9, `S84` INTEGER DEFAULT 9, `S85` INTEGER DEFAULT 9, " +
                "`S86` INTEGER DEFAULT 9, `S87` INTEGER DEFAULT 9, `S88` INTEGER DEFAULT 9, `S89` INTEGER DEFAULT 9, `S90` INTEGER DEFAULT 9, " +
                "`S91` INTEGER DEFAULT 9, `S92` INTEGER DEFAULT 9, `S93` INTEGER DEFAULT 9, `S94` INTEGER DEFAULT 9, `S95` INTEGER DEFAULT 9, " +
                "`S96` INTEGER DEFAULT 9, `S97` INTEGER DEFAULT 9, `S98` INTEGER DEFAULT 9, `S99` INTEGER DEFAULT 9, `S100` INTEGER DEFAULT 9, " +
                "`S101` INTEGER DEFAULT 9, `S102` INTEGER DEFAULT 9, `S103` INTEGER DEFAULT 9, `S104` INTEGER DEFAULT 9, `S105` INTEGER DEFAULT 9, " +
                "`S106` INTEGER DEFAULT 9, `S107` INTEGER DEFAULT 9, `S108` INTEGER DEFAULT 9, `S109` INTEGER DEFAULT 9, `S110` INTEGER DEFAULT 9, " +
                "`S111` INTEGER DEFAULT 9, `S112` INTEGER DEFAULT 9, `S113` INTEGER DEFAULT 9, `S114` INTEGER DEFAULT 9, `S115` INTEGER DEFAULT 9, " +
                "`S116` INTEGER DEFAULT 9, `S117` INTEGER DEFAULT 9, `S118` INTEGER DEFAULT 9, `S119` INTEGER DEFAULT 9, `S120` INTEGER DEFAULT 9 )");
    }

    public long firstfill(String user,String table,int subject,int branch,int year,int sem ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user",user);
        contentValues.put("tablename",table);
        contentValues.put("subject",subject);
        contentValues.put("branch",branch);
        contentValues.put("year",year);
        contentValues.put("sem",sem);
        long result = db.insert(tableRecord,null,contentValues);
        return result;
    }
    public Boolean updaterecord(int sno,int value,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("S"+String.valueOf(sno),value);
        long result = db.update(tableRecord,contentValues,"id="+id,null);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }
    public void updatestatus(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sync_status",1);
        long result = db.update(tableRecord,contentValues,"id="+id,null);
    }

    public Cursor getattendata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select * from `"+tableRecord+"` ",null);
        return res;
    }


   /* public boolean deleterow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableRecord, "id =" + id, null) > 0;
    }


    public void testtable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS `test` (`"+id+"` INTEGER PRIMARY KEY AUTOINCREMENT, `date` DATE DEFAULT CURRENT_DATE , `name` text )");

    }
    public long insterttext(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        long result = db.insert("test",null,contentValues);
        return result;
    }
    */
    public Cursor gethistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `branch`,`year`,`sem`,`subject`,`date`,`sync_status` from `"+tableRecord+"` ",null);
        return res;
    }



    public void get_personal_record(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS `personalrecord` (`ID` INTEGER PRIMARY KEY AUTOINCREMENT, `date` TEXT, `user` TEXT, `sub` INTEGER, `value` INTEGER )");
    }

    public boolean insetdata(String date,int subject,int data,String faculty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",date);
        contentValues.put("sub",subject);
        contentValues.put("value",data);
        contentValues.put("user",faculty);
        long result = db.insert("personalrecord",null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getdata(int sub){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `value` from `personalrecord` where `sub` like '"+sub+"' ",null);
        return res;
    }

    public void dropdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM `personalrecord`");
    }

}
