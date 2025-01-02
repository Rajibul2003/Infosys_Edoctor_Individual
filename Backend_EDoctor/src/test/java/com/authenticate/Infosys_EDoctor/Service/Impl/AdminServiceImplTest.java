package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.DTO.AppointmentRequest;
import com.authenticate.Infosys_EDoctor.Entity.Admin;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Repository.AdminRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import com.authenticate.Infosys_EDoctor.Service.PatientService;
import com.authenticate.Infosys_EDoctor.Service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AdminServiceImpl.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AdminServiceImplTest {
    @MockBean
    private AdminRepository adminRepository;

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PatientService patientService;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Test addAdmin(Admin); given AdminRepository save(Object) return Admin (default constructor); then return Admin (default constructor)")
    void testAddAdmin_givenAdminRepositorySaveReturnAdmin_thenReturnAdmin() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        admin.setMobileNo("Mobile No");
        admin.setName("Name");
        admin.setPassword("iloveyou");
        when(adminRepository.save(Mockito.<Admin>any())).thenReturn(admin);
        Optional<Admin> emptyResult = Optional.empty();
        when(adminRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);

        Admin admin2 = new Admin();
        admin2.setAdminId("42");
        admin2.setEmail("jane.doe@example.org");
        admin2.setMobileNo("Mobile No");
        admin2.setName("Name");
        admin2.setPassword("iloveyou");

        Admin actualAddAdminResult = adminServiceImpl.addAdmin(admin2);

        verify(adminRepository).findByEmail(eq("jane.doe@example.org"));
        verify(adminRepository).save(isA(Admin.class));
        assertSame(admin, actualAddAdminResult);
    }

    @Test
    @DisplayName("Test addAdmin(Admin); then throw RuntimeException")
    void testAddAdmin_thenThrowRuntimeException() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        admin.setMobileNo("Mobile No");
        admin.setName("Name");
        admin.setPassword("iloveyou");
        Optional<Admin> ofResult = Optional.of(admin);
        when(adminRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        Admin admin2 = new Admin();
        admin2.setAdminId("42");
        admin2.setEmail("jane.doe@example.org");
        admin2.setMobileNo("Mobile No");
        admin2.setName("Name");
        admin2.setPassword("iloveyou");

        assertThrows(RuntimeException.class, () -> adminServiceImpl.addAdmin(admin2));
        verify(adminRepository).findByEmail(eq("jane.doe@example.org"));
    }

    @Test
    @DisplayName("Test updateAdmin(String, Admin)")
    void testUpdateAdmin() {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setAdminId("42");
        updatedAdmin.setEmail("jane.doe@example.org");
        updatedAdmin.setMobileNo("Mobile No");
        updatedAdmin.setName("Name");
        updatedAdmin.setPassword("iloveyou");
        when(adminRepository.findById(Mockito.<String>any())).thenReturn(Optional.of(updatedAdmin));
        when(adminRepository.save(Mockito.<Admin>any())).thenReturn(updatedAdmin);

        adminServiceImpl.updateAdmin("42", updatedAdmin);

        verify(adminRepository).findById(eq("42"));
        verify(adminRepository).save(isA(Admin.class));
    }

    @Test
    @DisplayName("Test verifyAdmin(String)")
    void testVerifyAdmin() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        when(adminRepository.findById(Mockito.<String>any())).thenReturn(Optional.of(admin));

        adminServiceImpl.verifyAdmin("42");

        verify(adminRepository).findById(eq("42"));
    }

    @Test
    @DisplayName("Test deleteAdmin(String)")
    void testDeleteAdmin() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        when(adminRepository.findById(Mockito.<String>any())).thenReturn(Optional.of(admin));

        adminServiceImpl.deleteAdmin("42");

        verify(adminRepository).findById(eq("42"));
        verify(adminRepository).delete(isA(Admin.class));
    }

    @Test
    @DisplayName("Test getAllPatients()")
    void testGetAllPatients() {
        adminServiceImpl.getAllPatients();
        verify(patientService).getAllPatients();
    }

    @Test
    @DisplayName("Test updatePatient(String, Patient)")
    void testUpdatePatient() {
        Patient patient = new Patient();
        patient.setPatientId("42");
        patient.setName("John Doe");
        when(patientService.updateProfile(Mockito.<String>any(), Mockito.<Patient>any())).thenReturn(patient);

        adminServiceImpl.updatePatient("42", patient);

        verify(patientService).updateProfile(eq("42"), isA(Patient.class));
    }

    @Test
    @DisplayName("Test deletePatient(String)")
    void testDeletePatient() {
        adminServiceImpl.deletePatient("42");
        verify(patientService).deletePatient(eq("42"));
    }

    @Test
    @DisplayName("Test getAllDoctors()")
    void testGetAllDoctors() {
        adminServiceImpl.getAllDoctors();
        verify(doctorService).findAllDoctors();
    }

    @Test
    @DisplayName("Test updateDoctor(String, Doctor)")
    void testUpdateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId("42");
        doctor.setName("Dr. John");
        when(doctorService.updateDoctor(Mockito.<String>any(), Mockito.<Doctor>any())).thenReturn(doctor);

        adminServiceImpl.updateDoctor("42", doctor);

        verify(doctorService).updateDoctor(eq("42"), isA(Doctor.class));
    }

    @Test
    @DisplayName("Test deleteDoctor(String)")
    void testDeleteDoctor() {
        adminServiceImpl.deleteDoctor("42");
        verify(doctorService).deleteDoctor(eq("42"));
    }

    @Test
    @DisplayName("Test getAllAppointments()")
    void testGetAllAppointments() {
        adminServiceImpl.getAllAppointments();
        verify(appointmentService).getAllAppointments();
    }

    @Test
    @DisplayName("Test getAppointmentByPatientId(String)")
    void testGetAppointmentByPatientId() {
        adminServiceImpl.getAppointmentByPatientId("42");
        verify(appointmentService).getAppointmentsForPatient(eq("42"));
    }

    @Test
    @DisplayName("Test getAppointmentByDoctorId(String)")
    void testGetAppointmentByDoctorId() {
        adminServiceImpl.getAppointmentByDoctorId("42");
        verify(appointmentService).getAppointmentsForDoctor(eq("42"));
    }

    @Test
    @DisplayName("Test updateAppointment(Long, AppointmentRequest)")
    void testUpdateAppointment() {
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentDateTime("2020-03-01");
        appointmentRequest.setDoctorId("42");
        appointmentRequest.setPatientId("42");
        appointmentRequest.setReason("Just cause");
        adminServiceImpl.updateAppointment(1L, appointmentRequest);
        verify(appointmentService).updateAppointment(eq(1L), isA(com.authenticate.Infosys_EDoctor.Entity.Appointment.class));
    }

    @Test
    @DisplayName("Test addAppointment(AppointmentRequest)")
    void testAddAppointment() {
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentDateTime("2020-03-01");
        appointmentRequest.setDoctorId("42");
        appointmentRequest.setPatientId("42");
        appointmentRequest.setReason("Just cause");
        adminServiceImpl.addAppointment(appointmentRequest);
        verify(appointmentService).scheduleAppointment(isA(com.authenticate.Infosys_EDoctor.Entity.Appointment.class));
    }

    @Test
    @DisplayName("Test getPatientStatsById(String)")
    void testGetPatientStatsById() {
        adminServiceImpl.getPatientStatsById("42");
        verify(patientService).getPatientStatsById(eq("42"));
    }

    @Test
    @DisplayName("Test getAllPatientStats()")
    void testGetAllPatientStats() {
        adminServiceImpl.getAllPatientStats();
        verify(patientService).getAllPatientStats();
    }

    @Test
    @DisplayName("Test getAllDoctorStats()")
    void testGetAllDoctorStats() {
        adminServiceImpl.getAllDoctorStats();
        verify(doctorService).getAllDoctorStats();
    }

    @Test
    @DisplayName("Test getDoctorStatsById(String)")
    void testGetDoctorStatsById() {
        adminServiceImpl.getDoctorStatsById("42");
        verify(doctorService).getDoctorStatsById(eq("42"));
    }

    @Test
    @DisplayName("Test getWebStatsBetween(LocalDateTime, LocalDateTime)")
    void testGetWebStatsBetween() {
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        adminServiceImpl.getWebStatsBetween(startDate, LocalDate.of(1970, 1, 1).atStartOfDay());
        verify(adminServiceImpl).getWebStatsBetween(eq(startDate), eq(startDate));
    }

    @Test
    @DisplayName("Test getWebStats()")
    void testGetWebStats() {
        adminServiceImpl.getWebStats();
        verify(adminServiceImpl).getWebStats();
    }

    @Test
    @DisplayName("Test getAdminById(String)")
    void testGetAdminById() {
        adminServiceImpl.getAdminById("42");
        verify(adminRepository).findById(eq("42"));
    }

    @Test
    @DisplayName("Test addPatient(String, Patient)")
    void testAddPatient() {
        String patientUsername = "janedoe";
        Patient patient = new Patient();
        patient.setPatientId("42");

        // Mock the userService to return a user with the role PATIENT
        User mockUser = new User();
        mockUser.setUsername(patientUsername);
        mockUser.setRole(User.Role.PATIENT);
        mockUser.setPassword("password123");
        mockUser.setEmail("janedoe@example.com");

        when(userService.getUserByUsername(patientUsername)).thenReturn(mockUser);

        // Call the addPatient method
        adminServiceImpl.addPatient(patientUsername, patient);

        // Verify that the addPatient method of patientService is called with the correct patient
        verify(patientService).addPatient(eq(patient));
    }


}
