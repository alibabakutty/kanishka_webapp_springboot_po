package com.kanishkaa.purchase_order.employee_master.service.service_impl;

import com.kanishkaa.purchase_order.employee_master.dto.api_side.EmployeeMasterRequest;
import com.kanishkaa.purchase_order.employee_master.dto.response_side.EmployeeMasterResponse;
import com.kanishkaa.purchase_order.employee_master.dto.tally_json.EmployeeMasterInnerDto;
import com.kanishkaa.purchase_order.employee_master.dto.tally_json.EmployeeMasterWrapper;
import com.kanishkaa.purchase_order.employee_master.mapper.EmployeeMasterMapper;
import com.kanishkaa.purchase_order.employee_master.model.EmployeeMasterEntity;
import com.kanishkaa.purchase_order.employee_master.repository.EmployeeMasterRepository;
import com.kanishkaa.purchase_order.employee_master.service.EmployeeMasterService;
import com.kanishkaa.purchase_order.login.model.LoginModel;
import com.kanishkaa.purchase_order.login.model.Role;
import com.kanishkaa.purchase_order.login.repository.LoginRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeMasterServiceImpl implements EmployeeMasterService {

    private final EmployeeMasterRepository repository;
    private final EmployeeMasterMapper mapper;

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    // Excel formatter
    private final DataFormatter formatter = new DataFormatter();

    @Transactional
    @Override
    public EmployeeMasterResponse createEmployeeMaster(
            EmployeeMasterRequest request
    ) {
        return repository.findByEmployeeName(request.employeeName())
                .map(existingEntity -> {
                    mapper.updateEntityFromRequest(
                            request,
                            existingEntity
                    );
                    return mapper.toDto(
                            repository.save(existingEntity)
                    );
                })
                .orElseGet(() -> {
                    EmployeeMasterEntity newEntity =
                            mapper.toEntity(request);

                    EmployeeMasterEntity savedEmployee =
                            repository.save(newEntity);

                    createLoginForEmployee(
                            request,
                            savedEmployee
                    );

                    return mapper.toDto(savedEmployee);
                });
    }

    @Transactional
    @Override
    public void saveAllFromTally(
            EmployeeMasterWrapper wrapper
    ) {
        List<EmployeeMasterInnerDto> tallyData =
                wrapper.getEmployeeMasters();

        for (EmployeeMasterInnerDto innerDto : tallyData) {
            upsertFromDto(innerDto);
        }
    }

    @Transactional
    @Override
    public void bulkUploadEmployeeMasters(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                // Skip completely empty rows
                if (isRowEmpty(row)) {
                    System.out.println("Skipping empty row: " + (i + 1));
                    continue;
                }

                EmployeeMasterRequest request = new EmployeeMasterRequest(
                        getCellValue(row.getCell(0)), // employeeId
                        getCellValue(row.getCell(1)), // employeeName
                        getCellValue(row.getCell(2)), // mobileNumber
                        getDateCellValue(row.getCell(3)), // dateOfJoining
                        normalizeAadhaar(getCellValue(row.getCell(4))), // aadhaar
                        getCellValue(row.getCell(5)), // pan
                        getCellValue(row.getCell(6)), // email
                        getCellValue(row.getCell(7)), // password
                        null
                );

                System.out.println("----- Excel Row " + i + " -----");
                System.out.println("Employee ID: " + request.employeeId());
                System.out.println("Employee Name: " + request.employeeName());
                System.out.println("Mobile: " + request.mobileNumber());
                System.out.println("Date: " + request.dateOfJoining());
                System.out.println("Aadhaar: " + request.aadhaarNumber());
                System.out.println("Email: " + request.email());
                System.out.println("Password: " + request.password());

                createEmployeeMaster(request);
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Failed to process Excel file: " + e.getMessage()
            );
        }
    }

    @Override
    public EmployeeMasterResponse getByIdEmployeeMaster(
            Long id
    ) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Employee not found with id: "
                                        + id
                        )
                );
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeMasterResponse getByMobileNumber(
            String mobileNumber
    ) {
        return repository.findByMobileNumber(
                        mobileNumber
                )
                .map(mapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Employee not found with mobile: "
                                        + mobileNumber
                        )
                );
    }

    @Override
    public List<EmployeeMasterResponse>
    getAllEmployeeMasters() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeMasterResponse updateEmployeeMaster(
            Long id,
            EmployeeMasterRequest request
    ) {
        EmployeeMasterEntity existingEntity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Cannot update, ID not found: "
                                                + id
                                )
                        );

        mapper.updateEntityFromRequest(
                request,
                existingEntity
        );

        return mapper.toDto(
                repository.save(existingEntity)
        );
    }

    @Transactional
    @Override
    public EmployeeMasterResponse updateEmployeeByMobileNumber(String mobileNumber, EmployeeMasterRequest request){
        EmployeeMasterEntity existingEntity = repository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with this mobile number: " + mobileNumber));

        // update fields
        mapper.updateEntityFromRequest(request, existingEntity);
        EmployeeMasterEntity updated = repository.save(existingEntity);
        return mapper.toDto(updated);
    }

    @Transactional
    @Override
    public void deleteByIdEmployeeMaster(
            Long id
    ) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Cannot delete, ID not found: "
                            + id
            );
        }

        repository.deleteById(id);
    }

    private void createLoginForEmployee(
            EmployeeMasterRequest request,
            EmployeeMasterEntity employee
    ) {
        if (loginRepository.existsByUsername(
                request.employeeName()
        )) return;

        LoginModel login = new LoginModel();

        login.setUsername(
                request.employeeName()
        );

        login.setEmail(
                request.email()
        );

        login.setPassword(
                passwordEncoder.encode(
                        request.password()
                )
        );

        login.setRole(
                Role.EMPLOYEE
        );

        login.setEmployee(
                employee
        );

        loginRepository.save(login);
    }

    private void upsertFromDto(
            EmployeeMasterInnerDto dto
    ) {
        repository.findByEmployeeName(
                        dto.employeeName()
                )
                .ifPresentOrElse(
                        existing -> {
                            mapper.updateEntityFromDto(
                                    dto,
                                    existing
                            );
                            repository.save(existing);
                        },
                        () -> {
                            EmployeeMasterEntity entity =
                                    mapper.toEntity(dto);

                            repository.save(entity);
                        }
                );
    }

    // Safe string reader
    private String getCellValue(
            Cell cell
    ) {
        if (cell == null) return null;

        String value =
                formatter
                        .formatCellValue(cell)
                        .trim();

        return value.isEmpty()
                ? null
                : value;
    }

    // Safe date reader
    private LocalDate getDateCellValue(
            Cell cell
    ) {
        if (cell == null) return null;

        try {
            if (
                    cell.getCellType()
                            == CellType.NUMERIC
                            &&
                            DateUtil.isCellDateFormatted(
                                    cell
                            )
            ) {
                return cell
                        .getLocalDateTimeCellValue()
                        .toLocalDate();
            }

            String value =
                    formatter
                            .formatCellValue(cell)
                            .trim();

            if (!value.isEmpty()) {
                return LocalDate.parse(value);
            }

        } catch (Exception e) {
            System.out.println(
                    "Invalid date: "
                            + formatter
                            .formatCellValue(cell)
            );
        }

        return null;
    }

    // Remove Aadhaar spaces
    private String cleanAadhaar(
            String aadhaar
    ) {
        if (aadhaar == null) return null;

        return aadhaar
                .replaceAll("\\s+", "")
                .trim();
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);

            if (cell != null &&
                    cell.getCellType() != CellType.BLANK &&
                    !getCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }


    private String normalizeAadhaar(String aadhaar) {
        if (aadhaar == null) return null;

        // remove spaces
        return aadhaar.replaceAll("\\s+", "");
    }
}