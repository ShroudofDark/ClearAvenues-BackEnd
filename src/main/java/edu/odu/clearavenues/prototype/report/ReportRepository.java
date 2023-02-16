package edu.odu.clearavenues.prototype.report;

import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, Integer> {

    Report findByReportId(int id);
}
