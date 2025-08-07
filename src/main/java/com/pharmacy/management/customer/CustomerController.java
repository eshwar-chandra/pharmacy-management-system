package com.pharmacy.management.customer; // Updated package

import com.pharmacy.management.customer.CustomerDTO; // Updated import
import com.pharmacy.management.customer.CustomerService; // Updated import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody; // Alias to avoid name clash
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer Management", description = "APIs for managing customers")
@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Get a customer by their ID", description = "Fetches a customer based on their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer not found with the given ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "ID of the customer to retrieve", required = true) @PathVariable Long id) {
        log.info("API request to get customer by ID: {}", id);
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return customerDTO != null ? ResponseEntity.ok(customerDTO) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all customers", description = "Retrieves a list of all customers.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class)) })
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("API request to get all customers");
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Add a new customer", description = "Creates a new customer entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data for customer",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(
            @SwaggerRequestBody(description = "Customer data to create", required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody CustomerDTO customerDTO) {
        log.info("API request to add customer: {}", customerDTO.getName());
        CustomerDTO createdCustomer = customerService.addCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @Operation(summary = "Update an existing customer", description = "Updates details of an existing customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer not found with the given ID",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data for customer update",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to update", required = true) @PathVariable Long id,
            @SwaggerRequestBody(description = "Customer data to update", required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody CustomerDTO customerDTO) {
        log.info("API request to update customer with ID {}: {}", id, customerDTO.getName());
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return updatedCustomer != null ? ResponseEntity.ok(updatedCustomer) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a customer (soft delete)", description = "Marks a customer as deleted by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found with the given ID",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to delete", required = true) @PathVariable Long id) {
        log.info("API request to delete customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
