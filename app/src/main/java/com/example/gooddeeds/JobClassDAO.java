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

    @Query("SELECT * FROM job_tbl Where id not in (Select id From declined_job_tbl Where userGmail = :gmail) \n" +
            " ORDER BY CASE WHEN userGmail = :gmail THEN 1 ELSE 2 END, userGmail")
    List<Job> getAllJobs(String gmail);


    @Insert
    void insertJob(Job job);
    @Query("DELETE FROM job_tbl where id = :id")
    void deleteJobByID(String id);

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user_tbl WHERE email = :email")
    User getUserbyEmail(String email);

}
