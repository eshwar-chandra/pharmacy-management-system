package com.pharmacy.management.medicine; // Updated package

import com.pharmacy.management.medicine.MedicineDTO; // Updated import
import com.pharmacy.management.medicine.MedicineService; // Updated import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Medicine Management", description = "APIs for managing medicines")
@Slf4j
@RestController
@RequestMapping("/medicines") // This mapping might need to be /api/medicines if others are /api/*
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @Operation(summary = "Get a medicine by its ID", description = "Fetches a medicine based on its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved medicine",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicineDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Medicine not found with the given ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicineById(
            @Parameter(description = "ID of the medicine to retrieve", required = true) @PathVariable Long id) {
        log.info("Fetching medicine with ID: {}", id);
        MedicineDTO medicineDTO = medicineService.getMedicineById(id);
        return medicineDTO != null ? ResponseEntity.ok(medicineDTO) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all medicines", description = "Retrieves a list of all medicines.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of medicines",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MedicineDTO.class)) })
    @GetMapping
    public List<MedicineDTO> getAllMedicines() {
        log.info("Request to fetch all medicines");
        return medicineService.getAllMedicines();
    }

    @Operation(summary = "Add a new medicine", description = "Creates a new medicine entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicine created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicineDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input for medicine",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicineDTO addMedicine(@Valid @RequestBody MedicineDTO medicineDTO) {
        log.info("Adding new medicine: {}", medicineDTO);
        return medicineService.addMedicine(medicineDTO);
    }

    @Operation(summary = "Update an existing medicine", description = "Updates details of an existing medicine by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicineDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Medicine not found with the given ID",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input for medicine update",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicineDTO> updateMedicine(
            @Parameter(description = "ID of the medicine to update", required = true) @PathVariable Long id,
            @Valid @RequestBody MedicineDTO medicineDTO) {
        log.info("Updating medicine with ID {}: {}", id, medicineDTO);
        MedicineDTO updatedMedicine = medicineService.updateMedicine(id, medicineDTO);
        return updatedMedicine != null ? ResponseEntity.ok(updatedMedicine) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a medicine (soft delete)", description = "Marks a medicine as deleted by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medicine deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Medicine not found with the given ID",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteMedicine(
            @Parameter(description = "ID of the medicine to delete", required = true) @PathVariable Long id) {
        log.info("Deleting medicine with ID: {}", id);
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }
}
