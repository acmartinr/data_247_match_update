package models;

import services.db.entity.RegistrationRequest;

import java.util.List;

public class RegistrationRequestListResponse {

    private int total;
    private List<RegistrationRequest> requests;

    public RegistrationRequestListResponse() {}

    public RegistrationRequestListResponse(int total, List<RegistrationRequest> requests) {
        this.total = total;
        this.requests = requests;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List<RegistrationRequest> getRequests() { return requests; }
    public void setRequests(List<RegistrationRequest> requests) {  this.requests = requests; }
}
