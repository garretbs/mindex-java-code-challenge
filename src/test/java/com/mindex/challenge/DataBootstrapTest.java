package com.mindex.challenge;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.ReportingStructureRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataBootstrapTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReportingStructureRepository reportingStructureRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Test
    public void employeeTest() {
        Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Lennon", employee.getLastName());
        assertEquals("Development Manager", employee.getPosition());
        assertEquals("Engineering", employee.getDepartment());
    }

    @Test
    public void reportingStructureTest() {
        ReportingStructure reportingStructure = reportingStructureRepository.findByEmployeeEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotNull(reportingStructure);
        assertEquals("John", reportingStructure.getEmployee().getFirstName());
        assertEquals("Lennon", reportingStructure.getEmployee().getLastName());
        assertEquals("Development Manager", reportingStructure.getEmployee().getPosition());
        assertEquals("Engineering", reportingStructure.getEmployee().getDepartment());
    }

    @Test
    public void compensationTest() {
        Compensation insertCompensation = new Compensation();
        Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        insertCompensation.setEmployee(employee);
        insertCompensation.setSalary(100000);
        insertCompensation.setEffectiveDate(new Date(500000));
        compensationRepository.insert(insertCompensation);
        Compensation compensation = compensationRepository.findByEmployeeEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotNull(compensation);
        assertEquals(insertCompensation.getEmployee().getEmployeeId(), compensation.getEmployee().getEmployeeId());
        assertEquals(insertCompensation.getSalary(), compensation.getSalary());
        assertEquals(insertCompensation.getEffectiveDate(), compensation.getEffectiveDate());
    }
}