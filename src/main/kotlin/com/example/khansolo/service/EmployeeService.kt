package com.example.khansolo.service

import com.example.khansolo.repository.EmployeeRepository
import com.example.khansolo.model.CustomException
import com.example.khansolo.model.Employee
import io.micrometer.core.annotation.Timed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

/**
 * This class handles all CRUD (create, Read, Update, Delete Operations) service
 **/
@Service
class EmployeeService(
    @Autowired
    private val employeeRepository : EmployeeRepository,

    @Autowired
    private val validateEmailClient : ValidateEmailClient
) {

    fun getAllEmployees(): List<Employee> = employeeRepository.findAll()

    fun getEmployeeById(@PathVariable id: Long): Employee? = employeeRepository.findOneById(id)

    fun saveEmployees(@RequestBody employee: Employee): Employee {
        if(isLetters(employee.firstname) && isLetters(employee.lastname)){
            if(validateEmailClient.validateEmail(employee.email).format_valid)
               return employeeRepository.save(employee)
            else throw CustomException("Email is not valid")
        }
        else throw CustomException("Name cannot contains non alphabet characters")
    }

    fun updateEmployee(@PathVariable id: Long, @RequestBody employee: Employee): Employee {
        if(isLetters(employee.firstname) && isLetters(employee.lastname)) {
            if(validateEmailClient.validateEmail(employee.email).format_valid)
                employeeRepository.findOneById(id).takeIf { e -> e != null }?.let {
                    return employeeRepository.save(Employee(id, employee.email, employee.firstname, employee.lastname))
                } ?: throw CustomException("Employee does not exist")
            else throw CustomException("Email is not valid")
        }else throw CustomException("Name cannot contains non alphabet characters")

    }

    fun deleteEmployee(@PathVariable id : Long)  {
        try {
            employeeRepository.deleteById(id)
        }catch(e : EmptyResultDataAccessException){
            throw CustomException("id does not exist")
        }
    }

    fun isLetters(str: String): Boolean = str.all { it.isLetter() }
}