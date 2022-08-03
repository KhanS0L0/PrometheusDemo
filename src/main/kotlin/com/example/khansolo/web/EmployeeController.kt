package com.example.khansolo.web

import com.example.khansolo.model.CustomException
import com.example.khansolo.model.Employee
import com.example.khansolo.service.EmployeeService
import com.example.khansolo.util.Loggable
import io.micrometer.core.annotation.Timed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


/**
 * This class handles all the rest api requests (create, Read, Update, Delete Operations)
 **/
@RestController
class EmployeeController(
    @Autowired
    private val employeeService : EmployeeService,
): Loggable {

    //gets all employees
    @Timed(
        value = "get.all.employees.metrics",
        description = "Time taken to return employee list",
    )
    @GetMapping("/employees")
    fun getAllEmployees() : ResponseEntity<List<Employee>> =
            ResponseEntity.status(HttpStatus.OK).body(employeeService.getAllEmployees())


    //gets the requested employee
    @Timed(
        value = "get.employee.metrics",
        description = "Time taken to return employee by id"
    )
    @GetMapping("/employees/{id}")
    fun getEmployeeById(@PathVariable id : Long) : Employee =
            employeeService.getEmployeeById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This employee does not exist")

    //creates a new employee
    @Timed(
        value = "save.employee.metrics",
        description = "Time taken to save employee"
    )
    @PostMapping("/employees")
    fun saveEmployees(@RequestBody employee : Employee) : ResponseEntity<Employee>   {
        try {
            Thread.sleep(5000)
            return ResponseEntity.status(HttpStatus.OK).body(employeeService.saveEmployees(employee))
        }catch (e : CustomException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Input is not in valid format")
        }
    }

    //updates an existing employee
    @Timed(
        value = "update.employee.metrics",
        description = "Time taken to update employee"
    )
    @PutMapping("/employees/{id}")
    fun updateEmployee(@PathVariable id : Long, @RequestBody employee : Employee): ResponseEntity<Employee> {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateEmployee(id,employee))
        }catch (e : CustomException){
            if(e.message.equals("Employee does not exist"))
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found")
            else
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Input is not in valid format")
        }
    }

    // deletes an existing employee
    @Timed(
        value = "delete.employee.metrics",
        description = "Time taken to delete employee"
    )
    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id : Long) : ResponseEntity<Any>{
        try {
            employeeService.deleteEmployee(id)
        }
        catch (e: Exception) {
            log.warn("Failed to delete employee with id: $id")
            log.warn("Exception: $e")
            log.warn("Exception message: ${e.message}")
        }
        return ResponseEntity.noContent().build()
    }
}