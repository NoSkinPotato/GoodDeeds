package com.example.gooddeeds;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobClassDAO {

    @Query("Select * from user_tbl where email = :email")
    User CheckEmail(String email);

    @Query("Select * from job_tbl where id = :id")
    Job CheckJobID(String id);

    @Query("SELECT * FROM job_tbl ORDER BY \n" +
            "CASE WHEN userGmail = :gmail THEN 1 \n" +
            "        ELSE 2 \n" +
            "    END, \n" +
            "    userGmail")
    List<Job> getAllJobs(String gmail);


    @Insert
    void insertJob(Job job);

    @Query("DELETE FROM job_tbl")
    void deleteAll();

    @Query("DELETE FROM job_tbl where id = :id")
    void deleteJobByID(String id);

    @Query("SELECT COUNT(*) FROM job_tbl")
    int getCount();

    @Query("SELECT Max(id) FROM job_tbl")
    int getMax();

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user_tbl WHERE email = :email")
    User getUserbyEmail(String email);

}
