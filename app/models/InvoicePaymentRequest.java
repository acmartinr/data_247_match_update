package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class InvoicePaymentRequest {

    private int invoiceId;
    private String token;

    public InvoicePaymentRequest() {}

    public String getToken() { return token; }
    public void setToken( String token ) { this.token = token; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
}
