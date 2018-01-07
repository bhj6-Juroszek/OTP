package com.example.backend.controllersEntities.responses;

public class BookingResponse extends ResponseWithCode {
  private String reservationId;

  public String getReservationId() {
    return reservationId;
  }

  public void setReservationId(final String reservationId) {
    this.reservationId = reservationId;
  }
}
