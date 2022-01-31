package models;

import services.db.entity.Payment;
import services.db.entity.User;

import java.util.List;

public class PaymentListResponse {

    private int total;
    private List< Payment > payments;

    public PaymentListResponse() {}

    public PaymentListResponse( int total, List< Payment > payments ) {
        this.total = total;
        this.payments = payments;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List< Payment > getPayments() { return payments; }
    public void setPayments( List< Payment > payments ) { this.payments = payments; }

}
