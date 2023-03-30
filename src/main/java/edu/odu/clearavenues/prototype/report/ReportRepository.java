package edu.odu.clearavenues.prototype.report;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Integer> {

    Report findByReportId(int id);


    @Query(value = "SELECT * FROM reports WHERE submitter = :email", nativeQuery = true)
    List<Report> findBySubmitter(@Param("email") String email);
}
