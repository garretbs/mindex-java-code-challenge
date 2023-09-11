package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.ReportingStructureRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private ReportingStructureRepository reportingStructureRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Fetching reportingStructure with id [{}]", id);

        ReportingStructure reportingStructure = reportingStructureRepository.findByEmployeeEmployeeId(id);

        if (reportingStructure == null) {
            throw new RuntimeException("Invalid employeeId for reporting structure: " + id);
        }

        Employee populatedEmployee = populateEmployee(reportingStructure.getEmployee(), reportingStructure);
        reportingStructure.setEmployee(populatedEmployee);

        return reportingStructure;
    }

    private Employee populateEmployee(Employee employee, ReportingStructure reportingStructure) {
        Employee populatedEmployee = employeeRepository.findByEmployeeId(employee.getEmployeeId());
        List<Employee> directReports = populatedEmployee.getDirectReports();
        if (directReports != null) {
            // Iterate through the direct reports by index, as we will update entries in place
            for(int i = 0; i < directReports.size(); i++) {
                Employee directReport = employeeRepository.findByEmployeeId(directReports.get(i).getEmployeeId());
                directReport = populateEmployee(directReport, reportingStructure);
                directReports.set(i, directReport);
                reportingStructure.incrementNumberOfReports();
            }
        }
        return populatedEmployee;
    }
}
