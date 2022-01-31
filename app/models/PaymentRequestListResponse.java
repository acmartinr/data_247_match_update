package models;

import java.util.List;

public class PaymentRequestListResponse {

    private int total;
    private List<services.db.entity.PaymentRequest> paymentRequests;

    public PaymentRequestListResponse() {}

    public PaymentRequestListResponse(int total, List<services.db.entity.PaymentRequest> paymentRequests) {
        this.total = total;
        this.paymentRequests = paymentRequests;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List<services.db.entity.PaymentRequest> getPaymentRequests() { return paymentRequests; }
    public void setPaymentRequests(List<services.db.entity.PaymentRequest> paymentRequests) { this.paymentRequests = paymentRequests; }

}
