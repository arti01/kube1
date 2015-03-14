/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.wydruki;

import java.util.List;
 
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
 
/**
 *
 */
 
/**
 * @author Debasmita.Sahoo
 *
 */
@XmlRootElement(name="EmployeeData")
@XmlSeeAlso({Employee.class})
public class EmployeeData {
	public EmployeeData () {
	}
 
	private List employeeList;
 
	@XmlElementWrapper(name = "employeeList")
	@XmlElement(name = "employee")
	public List getEmployeeList() {
		return employeeList;
	}
	public void setEemployeeList (List employeeList) {
		this.employeeList = employeeList;
	}
 
}