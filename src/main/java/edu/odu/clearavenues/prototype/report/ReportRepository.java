package edu.odu.clearavenues.prototype.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Integer> {

    Report findByReportId(int id);

    List<Report> findBySubmittedBy(String email);
}
