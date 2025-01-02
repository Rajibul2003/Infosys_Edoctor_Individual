package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class ReminderSchedulerTest {

    @InjectMocks
    private ReminderScheduler reminderScheduler;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScheduleNextDayReminders_WithConfirmedAppointments() {
        // Arrange
        LocalDate nextDay = LocalDate.now().plusDays(1);

        Appointment appointment1 = new Appointment();
        appointment1.setStatus(Appointment.Status.Confirmed);

        Appointment appointment2 = new Appointment();
        appointment2.setStatus(Appointment.Status.Confirmed);

        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);

        when(appointmentService.getAppointmentsByDate(nextDay)).thenReturn(appointments);

        // Act
        reminderScheduler.scheduleNextDayReminders();

        // Assert
        verify(appointmentService, times(1)).getAppointmentsByDate(nextDay);
        verify(notificationService, times(1)).sendAppointmentReminder(appointment1);
        verify(notificationService, times(1)).sendAppointmentReminder(appointment2);
    }

    @Test
    void testScheduleNextDayReminders_WithNoAppointments() {
        // Arrange
        LocalDate nextDay = LocalDate.now().plusDays(1);

        when(appointmentService.getAppointmentsByDate(nextDay)).thenReturn(Collections.emptyList());

        // Act
        reminderScheduler.scheduleNextDayReminders();

        // Assert
        verify(appointmentService, times(1)).getAppointmentsByDate(nextDay);
        verifyNoInteractions(notificationService);
    }

    @Test
    void testScheduleNextDayReminders_WithNonConfirmedAppointments() {
        // Arrange
        LocalDate nextDay = LocalDate.now().plusDays(1);

        Appointment appointment1 = new Appointment();
        appointment1.setStatus(Appointment.Status.Pending);

        Appointment appointment2 = new Appointment();
        appointment2.setStatus(Appointment.Status.Cancelled);

        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);

        when(appointmentService.getAppointmentsByDate(nextDay)).thenReturn(appointments);

        // Act
        reminderScheduler.scheduleNextDayReminders();

        // Assert
        verify(appointmentService, times(1)).getAppointmentsByDate(nextDay);
        verifyNoInteractions(notificationService);
    }

    @Test
    void testScheduleNextDayReminders_MixedAppointmentStatuses() {
        // Arrange
        LocalDate nextDay = LocalDate.now().plusDays(1);

        Appointment confirmedAppointment = new Appointment();
        confirmedAppointment.setStatus(Appointment.Status.Confirmed);

        Appointment pendingAppointment = new Appointment();
        pendingAppointment.setStatus(Appointment.Status.Pending);

        Appointment cancelledAppointment = new Appointment();
        cancelledAppointment.setStatus(Appointment.Status.Cancelled);

        List<Appointment> appointments = Arrays.asList(confirmedAppointment, pendingAppointment, cancelledAppointment);

        when(appointmentService.getAppointmentsByDate(nextDay)).thenReturn(appointments);

        // Act
        reminderScheduler.scheduleNextDayReminders();

        // Assert
        verify(appointmentService, times(1)).getAppointmentsByDate(nextDay);
        verify(notificationService, times(1)).sendAppointmentReminder(confirmedAppointment);
        verifyNoMoreInteractions(notificationService);
    }
}
