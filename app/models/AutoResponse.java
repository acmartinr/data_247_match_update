package models;

import java.util.List;

public class AutoResponse {

    private int total;
    private List<String> models;
    private List<String> makes;

    public AutoResponse() {}

    public AutoResponse(int total,
                        List<String> models,
                        List<String> makes) {
        this.total = total;
        this.models = models;
        this.makes = makes;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    public List<String> getMakes() {
        return makes;
    }

    public void setMakes(List<String> makes) {
        this.makes = makes;
    }
}
