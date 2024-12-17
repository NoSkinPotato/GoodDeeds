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

    @Query("SELECT * FROM job_tbl Where id not in (Select id From declined_job_tbl Where userGmail = :gmail) and worker is null \n" +
            " ORDER BY CASE WHEN userGmail = :gmail THEN 1 ELSE 2 END, userGmail")
    List<Job> getAllJobs(String gmail);

    @Query("Update job_tbl set worker = :email where userGmail = :userGmail and id = :id")
    void AssignWorkerToJob(String email,String userGmail,String id);

    @Insert
    void insertJob(Job job);
    @Query("DELETE FROM job_tbl where id = :id")
    void deleteJobByID(String id);

    @Insert
    void InsertDeclineJob(DeclinedJob dj);

    @Query("Select * from user_tbl where email in (Select distinct CASE \n" +
            "        WHEN userGmail = :gmail AND worker IS NOT NULL THEN worker \n" +
            "        WHEN worker = :gmail THEN userGmail \n" +
            "    END From job_tbl where " +
            "(userGmail = :gmail and worker is not null) or (worker = :gmail))")
    List<User> getAllChatUsers(String gmail);

    @Insert
    void insertUser(User user);

    @Query("Select * from chat_tbl where (chatID like '%' || :email1 || '%' and chatID like '%' || :email2 || '%') Order by textID asc")
    List<Chat> getAllMessagesWithID(String email1, String email2);

    @Query("Select Max(textID) from chat_tbl where (chatID like '%' || :email1 || '%' and chatID like '%' || :email2 || '%')")
    int getMaxChat(String email1, String email2);

    @Insert
    void insertMessage(Chat chat);

    @Query("SELECT * FROM user_tbl WHERE email = :email")
    User getUserbyEmail(String email);

}
