package edu.odu.clearavenues.prototype.report;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Integer> {

    Report findByReportId(int id);


    @Query(value = "SELECT * FROM reports WHERE submitter = :email", nativeQuery = true)
    List<Report> findBySubmitter(@Param("email") String email);

    @Query(value = "SELECT count(*) FROM reports WHERE report_date >= date_sub(curdate(), interval 6 day) AND location_id = :locationId", nativeQuery = true)
    int getLast7DayReportCount(@Param("locationId") int locationId);

    @Query(value = "SELECT * FROM reports WHERE submitter = :submitter AND status = :status", nativeQuery = true)
    List<Report> getReportsBySubmitterAndStatus(@Param("submitter") String submitter, @Param("status") String status);
}
